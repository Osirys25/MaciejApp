package org.maciejowka.events.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.maciejowka.events.data.exception.MissingPosterHardcodedException;
import org.maciejowka.events.model.EventModel;
import org.maciejowka.events.poster.PosterHardcodedFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maciej on 28.09.17.
 */
public class WordpressEvent {
    private final JSONObject jsonObject;
    private static final String TITLE = "title";
    private static final String START_DATE = "start_date";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String TAGS = "tags";
    private static final String EMPTY_STRING = "";
    public static final List<String> posterTags = Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");


    public WordpressEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public EventModel toEventModel() {//FIXME move it to new parser class, or make this class parser...
        EventModel event = new EventModel();
        event.setTitle(getTitle());
        event.setDateAccurate(getDateAccurate());
        event.setDuration();
        event.setSummary(getSummary());
        setPosterHardcoded(event);
        event.setPosterUrl();
        return event;
    }

    private String getTitle() {
        try {
            return jsonObject.getString(TITLE);
        } catch (JSONException e) {
            return EMPTY_STRING;
        }
    }

    private String getDateAccurate() {
        try {
            return jsonObject.getString(START_DATE);
        } catch (JSONException e) {
            return EMPTY_STRING;
        }
    }

    private String getSummary() {
        try {
            String summaryHtml = jsonObject.getString(DESCRIPTION);
            return removeHtmlTags(summaryHtml);
        } catch (JSONException e) {
            return EMPTY_STRING;
        }
    }

    private String removeHtmlTags(String textWithHtml) {
        return Jsoup.parse(textWithHtml).text();
    }

    private void setPosterHardcoded(EventModel event) {
        try {
            String posterHardcoded = getPosterHardcoded();
            event.setPosterHardcoded(posterHardcoded);
        } catch (JSONException | MissingPosterHardcodedException e) {
            ;
        }
    }

    private String getPosterHardcoded() throws JSONException, MissingPosterHardcodedException {
        List<String> hardcodedTags = PosterHardcodedFactory.posterTags;
        List<String> eventTags = getEventTags();
        for (String posterTag : hardcodedTags) {
            if (eventTags.contains(posterTag)) {
                return posterTag;
            }
        }
        throw new MissingPosterHardcodedException();
    }

    private List<String> getEventTags() throws JSONException {
        List<String> tags = new ArrayList<>();
        JSONArray tagsJson = jsonObject.getJSONArray(TAGS);
        for (int i = 0; i < tagsJson.length(); i++) {
            tags.add(tagsJson.getJSONObject(i).getString(NAME));
        }
        return tags;
    }

    private List<Integer> getDaysOfWeek() {
        List<String> eventTags = getEventTags();

    }


}
