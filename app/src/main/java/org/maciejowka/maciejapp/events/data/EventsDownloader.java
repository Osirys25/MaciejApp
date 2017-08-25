package org.maciejowka.maciejapp.events.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by maciej on 12.09.17.
 */
public class EventsDownloader {

    private static final String FAILURE_MESSAGE = "downloading events json failure";
    private static final String REQUEST_AGENT = "User-Agent";
    private static final String REQUEST_BROWSER = "Mozilla/5.0";
    private final String url;
    private final int attempts;

    public EventsDownloader(String url, int attempts) {
        this.url = url;
        this.attempts = attempts;
    }

    public Result<String> download() {
        boolean success = false;
        String eventsJson = null;

        for (int i = 0; i < attempts && !success; i++) {
            try {
                eventsJson = performAttempt();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (success) {
            return Result.success(eventsJson);
        } else {
            return Result.failure(FAILURE_MESSAGE);
        }

    }

    private String performAttempt() throws IOException {
        URLConnection connection = getConnection();
        return downloadDataFromConnection(connection);
    }

    private URLConnection getConnection() throws IOException {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setRequestProperty(REQUEST_AGENT, REQUEST_BROWSER);
        return connection;
    }

    private String downloadDataFromConnection(URLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        return response.toString();
    }

}
