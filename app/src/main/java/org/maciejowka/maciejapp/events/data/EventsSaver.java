package org.maciejowka.maciejapp.events.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by maciej on 12.09.17.
 */
public class EventsSaver{
    private final String json;
    private final Context context;
    private static final String SHARED_PREFS = "EventsSaver_SharedPreferences";
    private static final String EVENT_LIST_JSON_KEY = "List<EventModel>_key";

    public EventsSaver(String json, Context context) {
        this.json = json;
        this.context = context;
    }

   public void save() {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EVENT_LIST_JSON_KEY, json);
        editor.apply();
    }

}
