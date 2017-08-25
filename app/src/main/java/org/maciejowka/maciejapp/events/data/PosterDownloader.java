package org.maciejowka.maciejapp.events.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by maciej on 13.09.17.
 */
public class PosterDownloader {

    private final String url;
    private final int attempts;
    private static final String REQUEST_AGENT = "User-Agent";
    private static final String REQUEST_BROWSER = "Mozilla/5.0";
    private static final String FAILURE_MESSAGE = "downloading poster failure";

    public PosterDownloader(String url, int attempts) {
        this.url = url;
        this.attempts = attempts;
    }

    public Result<Bitmap> download() {
        boolean success = false;
        Bitmap poster = null;

        for (int i = 0; i < attempts && !success; i++) {
            try {
                poster = performAttempt();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (success) {
            return Result.success(poster);
        } else {
            return Result.failure(FAILURE_MESSAGE);
        }
    }

    private Bitmap performAttempt() throws IOException {
        URLConnection connection = getConnection();
        return downloadDataFromConnection(connection);
    }

    private URLConnection getConnection() throws IOException {
        URL website = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) website.openConnection();
        connection.setRequestProperty(REQUEST_AGENT, REQUEST_BROWSER);
        connection.setDoInput(true);
        connection.connect();
        return connection;
    }

    private Bitmap downloadDataFromConnection(URLConnection connection) throws IOException {
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;
    }

}
