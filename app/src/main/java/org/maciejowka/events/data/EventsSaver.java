package org.maciejowka.events.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by maciej on 12.09.17.
 */
class EventsSaver {
    private final String json;
    private final Context context;
    private static final String SHARED_PREFS = "EventsSaver_SharedPreferences";
    private static final String EVENT_LIST_JSON_KEY = "List<EventModel>_key";

    EventsSaver(String json, Context context) {
        this.json = json;
        this.context = context;
    }

    void save() {
        SharedPreferences.Editor editor = getSharedPrefsEditor();
        editor.putString(EVENT_LIST_JSON_KEY, json);
        editor.apply();
    }

    private SharedPreferences.Editor getSharedPrefsEditor() {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit();
    }

}
