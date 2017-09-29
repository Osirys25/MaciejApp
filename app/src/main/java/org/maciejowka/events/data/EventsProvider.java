package org.maciejowka.events.data;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import org.maciejowka.events.EventsFragment;
import org.maciejowka.events.data.exception.ApiJsonParserException;
import org.maciejowka.events.data.exception.JSONDownloaderException;
import org.maciejowka.events.model.EventDate;
import org.maciejowka.events.model.EventDateException;
import org.maciejowka.events.model.EventModel;
import org.maciejowka.events.model.EventDateComparator;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by maciej on 12.09.17.
 */
public class EventsProvider extends AsyncTask<Void, Void, List<EventModel>> {

    private static final String EVENTS_CYCLIC_SOURCE = "http://www.maciejowka.org/wp-json/tribe/events/v1/events?tags[]=cyclic";
    private static final String EVENTS_TODAY_AND_TOMORROW_SOURCE_SCHEMA =
            "http://www.maciejowka.org/wp-json/tribe/events/v1/events?start_date=%s&end_date=%s&categories[]=app";
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final EventsFragment eventsFragment;

    public EventsProvider(EventsFragment fragment) {
        this.eventsFragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Result<List<EventModel>> result) {
        handleResult(result);
        super.onPostExecute(result);
    }

    private void handleResult(Result<List<EventModel>> result) {
        if (result.isSuccess()) {
            List<EventModel> events = result.getData();
            sortEvents(events);
            List<EventModel> eventsTodayAndTomorrow = getTodayAndTomorrowEvents(events);
            updateActivity(eventsTodayAndTomorrow);
        } else {
            String msg = result.getMessage();
            Log.i("EventsProvider", msg);
        }
    }

    private void sortEvents(List<EventModel> events) {
        Collections.sort(events, new EventDateComparator());
    }

    private List<EventModel> getTodayAndTomorrowEvents(List<EventModel> events) {
        List<EventModel> eventsTodayAndTomorrow = new ArrayList<>();
        for (EventModel event : events) {
            addTodayOrTomorrowEvent(eventsTodayAndTomorrow, event);
        }
        return eventsTodayAndTomorrow;
    }

    private void addTodayOrTomorrowEvent(List<EventModel> events, EventModel event) {
        try {
            if (new EventDate(event).isTodayOrTomorrow()) {
                events.add(event);
            }
        } catch (EventDateException e) {
            e.printStackTrace();
        }
    }

    private void updateActivity(List<EventModel> events) {
        if (eventsFragment != null) {
            eventsFragment.update(events);
        }
    }

    private void provideDebugger() {
        if (android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
    }

    private Result<List<EventModel>> handleEventsJsonDownloadSuccess(Result<String> result) {
        String eventsJson = result.getData();
        Result<List<EventModel>> parsingEventsFromJsonResult = parseEventsFromJson(eventsJson);
        if (parsingEventsFromJsonResult.isSuccess()) {
            return handleEventsFromJsonParsingSuccess(parsingEventsFromJsonResult, eventsJson);
        } else {
            return getEventsFromMemory();
        }
    }

    private Result<List<EventModel>> handleEventsFromJsonParsingSuccess(Result<List<EventModel>> result, String eventsJson) {
        List<EventModel> events = result.getData();
        saveEventsInMemory(eventsJson);
        return Result.success(events);
    }

    private void saveEventsInMemory(String eventsJson) {
        Context context = eventsFragment.getApplicationContext();
        new EventsSaver(eventsJson, context).save();
    }

    private Result<List<EventModel>> getEventsFromMemory() {
        Result<String> readEventsResult = readEventsFromMemory();
        if (readEventsResult.isSuccess()) {
            String json = readEventsResult.getData();
            return parseEventsFromJson(json);
        } else {
            return Result.failure();
        }
    }

    private Result<List<EventModel>> parseEventsFromJson(String json) {
        return new EventsFromJsonParser(json).parse();
    }

    private Result<String> readEventsFromMemory() {
        Context context = eventsFragment.getApplicationContext();
        return new EventsReader(context).read();
    }

    @Override
    protected List<EventModel> doInBackground(Void[] params) {
        provideDebugger();
        return getEvents();
    }

    private List<EventModel> getEvents(){
        try{
            return downloadEvents();
        }
        catch (Exception e){
            return getEventsFromMemory();
        }
    }

    private List<EventModel> downloadEvents() throws JSONDownloaderException{
        String eventsCyclicJson = downloadCyclicEventsJson();
        String eventsTodayAndTomorrowJson = downloadTodayAndTomorrowEventsJson();
        List<WordpressEvent> eventsWordpress = getWordpressEventsFromApiJsons(eventsCyclicJson, eventsTodayAndTomorrowJson);
        //filter cyclic events !
    }

    List<WordpressEvent> getWordpressEventsFromApiJsons(String cyclicEventsJson, String todayAndTomorrowEventsJson) throws ApiJsonParserException{
        return new ApiJsonParser(cyclicEventsJson, todayAndTomorrowEventsJson).parseToWordpressEvents();
    }

    private String downloadCyclicEventsJson() throws JSONDownloaderException{ //or catch here and return empty_string
            return new JSONDownloader(EVENTS_CYCLIC_SOURCE).download();
    }

    private String downloadTodayAndTomorrowEventsJson() throws JSONDownloaderException{ //or catch here and return empty_string
        String source = getEventsTodayAndTomorrowSource();
        return new JSONDownloader(source).download();
    }
//TODO tworzenie source urls wyrzucić do factory czy coś takiego
    private String getEventsTodayAndTomorrowSource(){
        String dateToday = getDateToday();
        String dateTomorrow = getDateTomorrow();
        return String.format(EVENTS_TODAY_AND_TOMORROW_SOURCE_SCHEMA, dateToday, dateTomorrow);
    }

    private String getDateToday(){
        return getDate(new Date());
    }

    private String getDateTomorrow(){
        return getDate(getTomorrowDate());
    }

    private Date getTomorrowDate() {
        return new Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS);
    }

    private String getDate(Date eventDateTime) {
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateOnlyFormat.format(eventDateTime);
    }

}
