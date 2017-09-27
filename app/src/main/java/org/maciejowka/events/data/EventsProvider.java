package org.maciejowka.events.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.maciejowka.events.EventsFragment;
import org.maciejowka.events.model.EventDate;
import org.maciejowka.events.model.EventDateException;
import org.maciejowka.events.model.EventModel;
import org.maciejowka.events.model.EventDateComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by maciej on 12.09.17.
 */
public class EventsProvider extends AsyncTask<Void, Void, Result<List<EventModel>>> {

    private static final String EVENTS_SOURCE = "https://raw.githubusercontent.com/mcPear/hello-word/master/events.json";
    private static final int DOWNLOADING_ATTEMPTS = 5;
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

    @Override
    protected Result<List<EventModel>> doInBackground(Void[] params) {
        provideDebugger();
        Result<String> eventsJsonDownloadResult = downloadEvents();
        if (eventsJsonDownloadResult.isSuccess()) {
            return handleEventsJsonDownloadSuccess(eventsJsonDownloadResult);
        } else {
            return getEventsFromMemory();
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

    private Result<String> downloadEvents() {
        return new EventsDownloader(EVENTS_SOURCE, DOWNLOADING_ATTEMPTS).download();
    }

}
