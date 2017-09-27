package org.maciejowka.events.model;

import android.graphics.Bitmap;

/**
 * Created by maciej on 25.08.17.
 */
public class EventModel {
    private String title;
    private String dateAccurate;
    private String duration;
    private String summary;
    private String posterHardcoded;
    private String posterUrl;

    public EventModel(String title, String dateAccurate, String duration, String summary, String posterHardcoded, String posterUrl, Bitmap poster) {
        this.title = title;
        this.dateAccurate = dateAccurate;
        this.duration = duration;
        this.summary = summary;
        this.posterHardcoded = posterHardcoded;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateAccurate() {
        return dateAccurate;
    }

    public void setDateAccurate(String dateAccurate) {
        this.dateAccurate = dateAccurate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPosterHardcoded() {
        return posterHardcoded;
    }

    public void setPosterHardcoded(String posterHardcoded) {
        this.posterHardcoded = posterHardcoded;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public boolean hasPosterUrl() {
        return posterUrl != null;
    }

}
