package org.maciejowka.maciejapp.events.model;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maciej on 22.09.17.
 */
public class EventDate {

    private final static String TODAY_PL = "dzisiaj";
    private final static String TOMORROW_PL = "jutro";
    private final static String DATE_FORMAT = "yyyy/MM/dd";
    private final static String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";
    private final static String TIME_FORMAT = "HH:mm";

    private final EventModel event;

    public EventDate(EventModel event) {
        this.event = event;
    }

    public boolean isTodayOrTomorrow() throws EventDateException {
        try {
            return isToday() || isTomorrow();
        } catch (ParseException e) {
            throw new EventDateException(e.getMessage());
        }
    }

    public boolean isToday() throws ParseException {
        Date eventDateTime = getEventDateTime();
        String eventDate = getDateOnly(eventDateTime);
        String todayDate = getDateOnly(new Date());
        return eventDate.equals(todayDate);
    }

    public boolean isTomorrow() throws ParseException {
        Date eventDateTime = getEventDateTime();
        String eventDate = getDateOnly(eventDateTime);
        String tomorrowDate = getDateOnly(getTomorrowDate());
        return eventDate.equals(tomorrowDate);
    }

    public String getDateTimeOutput() {
        try {
            return getDayWord() + " " + getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return event.getDateAccurate();
        }
    }

    private String getDayWord() throws ParseException {
        if (isToday()) {
            return TODAY_PL;
        } else if (isTomorrow()) {
            return TOMORROW_PL;
        } else {
            return event.getDateAccurate();
        }
    }

    private Date getTomorrowDate() {
        return new Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS);
    }

    private String getDateOnly(Date eventDateTime) {
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateOnlyFormat.format(eventDateTime);
    }

    public Date getEventDateTime() throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        return dateTimeFormat.parse(event.getDateAccurate());
    }

    private String getTime() throws ParseException {
        Date eventDateTime = getEventDateTime();
        return getTimeOnly(eventDateTime);
    }

    private String getTimeOnly(Date eventDateTime) {
        SimpleDateFormat timeOnlyFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return timeOnlyFormat.format(eventDateTime);
    }
}
