package org.maciejowka.events.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by maciej on 13.09.17.
 */
class EventsReader {
    private final Context context;
    private static final String SHARED_PREFS = "EventsSaver_SharedPreferences";
    private static final String EVENT_LIST_JSON_KEY = "List<EventModel>_key";
    private static final String EVENT_LIST_JSON_DEFAULT_VALUE = "List<EventModel>_json_default_value";
    private static final String FAILURE_MESSAGE = "reading events from memory failure";

    EventsReader(Context context) {
        this.context = context;
    }

    Result<String> read() {
        SharedPreferences sharedPrefs = getSharedPrefs();
        String eventsJson = sharedPrefs.getString(EVENT_LIST_JSON_KEY, EVENT_LIST_JSON_DEFAULT_VALUE);
        if (eventsJson.equals(EVENT_LIST_JSON_DEFAULT_VALUE)) {
            return Result.failure(FAILURE_MESSAGE);
        } else {
            return Result.success(eventsJson);
        }
    }

    private SharedPreferences getSharedPrefs() {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
