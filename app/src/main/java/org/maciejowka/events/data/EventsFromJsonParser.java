package org.maciejowka.events.data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.maciejowka.events.model.EventModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by maciej on 12.09.17.
 */
public class EventsFromJsonParser {

    private final String json;
    private static final String FAILURE_MESSAGE = "parsing events from json failure";

    public EventsFromJsonParser(String json) {
        this.json = json;
    }

    public Result<List<EventModel>> parse() {
        Gson gson = new Gson();
        List<EventModel> events;
        try {
            events = Arrays.asList(gson.fromJson(json, EventModel[].class));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Result.failure(FAILURE_MESSAGE);
        }
        return Result.success(events);
    }

}
