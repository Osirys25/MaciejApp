package org.maciejowka.events.data;


import org.json.JSONException;
import org.json.JSONObject;
import org.maciejowka.events.model.EventModel;

/**
 * Created by maciej on 28.09.17.
 */
public class WordpressEvent {
    private final JSONObject jsonObject;
    private static final String TITLE = "title";
    private static final String START_DATE = "start_date";
    private static final String EMPTY_STRING = "";

    public WordpressEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public EventModel toEventModel(){
        EventModel event = new EventModel();
        event.setTitle(getTitle());
        event.setDateAccurate(getDateAccurate());
        event.setDuration();
        event.setSummary();
        event.setPosterHardcoded();
        event.setPosterUrl();
        return event;
    }

    private String getTitle(){
        try {
            return jsonObject.getString(TITLE);
        }
        catch (JSONException e){
            return EMPTY_STRING;
        }
    }

    private String getDateAccurate(){
        try {
            return jsonObject.getString(START_DATE);
        }
        catch (JSONException e){
            return EMPTY_STRING;
        }
    }

    private String getSummary(){

    }




}
