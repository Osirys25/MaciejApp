package org.maciejowka.notices;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static org.maciejowka.notices.LoadingStatus.DONE;
import static org.maciejowka.notices.LoadingStatus.DOWNLOADED;
import static org.maciejowka.notices.LoadingStatus.DOWNLOADING;
import static org.maciejowka.notices.LoadingStatus.RESTORED;

class NoticesLoader extends AsyncTask<LoadingStatus, Void, LoadingStatus> {

    private static final String NOTICES_JSON_URL = "http://www.maciejowka.org/klepson/get_category_posts/?slug=ogloszenia";
    private static final String SHARED_PREFERENCES_NAME="notices";

    private NoticesFragment noticesFragment;
    private Notices notices = new Notices();
    private boolean isRunning = true;
    private SharedPreferences sharedPreferences;

    void stop() {
        isRunning = false;
        cancel(true);
    }

    NoticesLoader(NoticesFragment noticesFragment) {
        this.noticesFragment = noticesFragment;
        sharedPreferences = noticesFragment.getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }

    private void toObject(String json) {
        JsonObject jsonObject = new JsonParser()
                .parse(json).getAsJsonObject()
                .get("posts").getAsJsonArray()
                .get(0).getAsJsonObject();

        notices = new Gson().fromJson(jsonObject, Notices.class);
    }

    @Override
    protected LoadingStatus doInBackground(LoadingStatus... loadingStatuses) {
        LoadingStatus loadingStatus = loadingStatuses[0];
        switch (loadingStatus) {
            case RESTORING : {
                if (loadFromSharedPreferences()) {
                    loadingStatus = RESTORED;
                    break;
                }
            }
            case DOWNLOADING : {
                if (new Network().isInternetAvailable()) {
                    try {
                        HttpURLConnection connection = new Network().getConnection(NOTICES_JSON_URL);
                        String json = new Network().downloadString(connection);
                        toObject(json);
                        saveToSharedPreferences();
                        loadingStatus = DOWNLOADED;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loadingStatus = DONE;
                }
            } break;
            default : {
                return DONE;
            }
        }
        notices.addMetadata();
        return loadingStatus;
    }

    private void saveToSharedPreferences() {
        notices.saveToSharedPreferences(sharedPreferences);
    }

    private boolean loadFromSharedPreferences() {
        notices.loadFromSharedPreferences(sharedPreferences);
        return notices.getId() != -1;
    }

    @Override
    protected void onPostExecute(LoadingStatus loadingStatus) {
        if (isRunning) {
            switch (loadingStatus) {
                case RESTORED : {
                    noticesFragment.loadData(notices);
                    noticesFragment.startLoading(DOWNLOADING);
                } break;
                case DOWNLOADED : {
                    noticesFragment.loadData(notices);
                } break;
                default : {
                    noticesFragment.stopRefreshing();
                }
            }
        }
    }
}
