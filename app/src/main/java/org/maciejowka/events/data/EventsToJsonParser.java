package org.maciejowka.events.data;

import com.google.gson.Gson;
import org.maciejowka.events.model.EventModel;

import java.util.List;

/**
 * Created by maciej on 13.09.17.
 */
public class EventsToJsonParser {
    private final List<EventModel> events;

    public EventsToJsonParser(List<EventModel> events) {
        this.events = events;
    }

    public String parse() {
        Gson gson = new Gson();
        return gson.toJson(events);
    }
}
