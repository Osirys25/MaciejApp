package com.example.root.maciejapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by maciej on 17.02.17.
 */
public class JSONOperator {

    private static final String currentURL="http://www.maciejowka.org/klepson/get_category_posts/?slug=ogloszenia-duszpasterskie";
    private static final String archivalURL="http://www.maciejowka.org/klepson/get_category_posts/?slug=archiwum";
    private static int publicationsNumber;
    private static JSONObject currentPublication;
    private static JSONObject archivalPublications;
    private static String[][] pubsDataArray;


    public static boolean completelyLoadPublications(){
        loadPublicationsToJSONObjects();
        return JSONObjectsToDataArray();
    }

    public static boolean JSONObjectsToDataArray(){

       try {
           publicationsNumber = archivalPublications.getInt("count")+1; // +1 means current publication
           pubsDataArray = new String[publicationsNumber][];

           pubsDataArray[0] = new String[2]; //2 because will contain title and content Strings
           pubsDataArray[0][0] = currentPublication.getJSONArray("posts").getJSONObject(0).getString("title");
           pubsDataArray[0][1] = currentPublication.getJSONArray("posts").getJSONObject(0).getString("content");

           for(int ii=1; ii<publicationsNumber; ii++){
               pubsDataArray[ii]=new String[2]; //2 because will contain title and content Strings
               pubsDataArray[ii][0] = archivalPublications.getJSONArray("posts").getJSONObject(ii-1).getString("title");
               pubsDataArray[ii][1] = archivalPublications.getJSONArray("posts").getJSONObject(ii-1).getString("content");
           }

       }
       catch(Exception e){
           e.printStackTrace();
           return false;
       }

       return true;
    }

    public static boolean loadPublicationsToJSONObjects(){
        //returns false if failed

        try {
            String currentPubJSON = getJSONfromURL(currentURL);
            String archivalPubJSON = getJSONfromURL(archivalURL);
            currentPublication = new JSONObject(currentPubJSON);
            archivalPublications = new JSONObject(archivalPubJSON);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }


    public static String getJSONfromURL(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    public static int getpubsNumber(){
        return publicationsNumber;
    }

    public static String[][] getPubsDataArray() {
        return pubsDataArray;
    }














}

