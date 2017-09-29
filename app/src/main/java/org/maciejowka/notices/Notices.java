package org.maciejowka.notices;

import android.content.SharedPreferences;

class Notices implements SharedPreferencesManager {

    private int id;
    private String title;
    private String content;
    private String modified;

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    String getModified() {
        return modified;
    }

    void setModified(String modified) {
        this.modified = modified;
    }

//    @Override
//    public boolean equals(Object obj) {
//        return super.equals(obj);
//    }

    @Override
    public void saveToSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", getId());
        editor.putString("title", getTitle());
        editor.putString("content", getContent());
        editor.putString("modified", getModified());

        editor.apply();
    }

    @Override
    public void loadFromSharedPreferences(SharedPreferences sharedPreferences) {
        id = sharedPreferences.getInt("id", -1);
        title = sharedPreferences.getString("title", "Ogłoszenia");
        content = sharedPreferences.getString("content", "");
        modified = sharedPreferences.getString("modified", "");
    }

    void addMetadata() {
        content = new StringBuilder()
                .append("<!DOCTYPE html><html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body>")
                .append(content)
                .append("</body></html>")
                .toString();
    }
}
