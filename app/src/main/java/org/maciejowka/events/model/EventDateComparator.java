package org.maciejowka.events.model;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by maciej on 10.09.17.
 */
public class EventDateComparator implements Comparator<EventModel> {


    @Override
    public int compare(EventModel first, EventModel second) {
        try {
            Date firstDate = getDate(first);
            Date secondDate = getDate(second);
            return firstDate.compareTo(secondDate);
        } catch (ParseException e) {
            return 0;
        }
    }

    private Date getDate(EventModel event) throws ParseException {
        return new EventDate(event).getEventDateTime();
    }

}
