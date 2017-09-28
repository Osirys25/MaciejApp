package org.maciejowka.events.data;

import org.maciejowka.events.data.exception.JSONDownloaderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by maciej on 12.09.17.
 */
public class JSONDownloader {

    private static final String REQUEST_AGENT = "User-Agent";
    private static final String REQUEST_BROWSER = "Mozilla/5.0";
    private final String url;

    public JSONDownloader(String url) {
        this.url = url;
    }

    public String download() throws JSONDownloaderException {
        try {
            return tryToDownload();
        } catch (IOException e) {
            throw new JSONDownloaderException(e.getMessage());
        }
    }

    private String tryToDownload() throws IOException {
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
