package org.maciejowka.maciejapp.events;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by maciej on 25.08.17.
 */
public class EventModel {
    private String title;
    private String date;
    private String duration;
    private String summary;
    private BitmapDrawable poster;
    private String posterUrl;

    public EventModel(String title, String date, String duration, String summary, BitmapDrawable poster, String posterUrl) {
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.summary = summary;
        this.poster = poster;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public BitmapDrawable getPoster() {
        return poster;
    }

    public void setPoster(BitmapDrawable poster) {
        this.poster = poster;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
