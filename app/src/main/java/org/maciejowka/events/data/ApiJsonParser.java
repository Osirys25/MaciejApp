package org.maciejowka.events.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.maciejowka.events.data.exception.ApiJsonParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciej on 28.09.17.
 */
public class ApiJsonParser {
    private final String cyclicEventsJson;
    private final String todayAndTomorrowEventsJson;
    private static final String EVENTS_JSON_ARRAY = "events";

    public ApiJsonParser(String cyclicEventsJson, String todayAndTomorrowEventsJson) {
        this.cyclicEventsJson = cyclicEventsJson;
        this.todayAndTomorrowEventsJson = todayAndTomorrowEventsJson;
    }

    public List<WordpressEvent> parseToWordpressEvents() throws ApiJsonParserException {
        try {
            JSONObject cyclicEventsFromApi = new JSONObject(cyclicEventsJson);
            JSONObject todayAndTomorrowEventsFromApi = new JSONObject(todayAndTomorrowEventsJson);
            JSONArray cyclicEvents = cyclicEventsFromApi.getJSONArray(EVENTS_JSON_ARRAY);
            JSONArray todayAndTomorrowEvents = todayAndTomorrowEventsFromApi.getJSONArray(EVENTS_JSON_ARRAY);
            return parseJSONObjectsToWordpressEvents(cyclicEvents, todayAndTomorrowEvents);
        } catch (JSONException e) {
            throw new ApiJsonParserException(e.getMessage());
        }
    }

    private List<WordpressEvent> parseJSONObjectsToWordpressEvents(JSONArray cyclicEvents, JSONArray todayAndTomorrowEvents) throws JSONException {
        List<WordpressEvent> wordpressEvents = new ArrayList<>();
        for (int i = 0; i < cyclicEvents.length(); i++) {
            wordpressEvents.add(new WordpressEvent(cyclicEvents.getJSONObject(i)));
        }
        for (int i = 0; i < todayAndTomorrowEvents.length(); i++) {
            wordpressEvents.add(new WordpressEvent(todayAndTomorrowEvents.getJSONObject(i)));
        }
        return wordpressEvents;
    }
}
