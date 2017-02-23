package com.example.root.maciejapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by maciej on 17.02.17.
 */
public class PublicationsLoader extends AsyncTask<Void,Void,PublicationsLoader.PublicationsStatus> {
//TODO execute po włączeniu neta
    private static final String currentURL="http://www.maciejowka.org/klepson/get_category_posts/?slug=ogloszenia-duszpasterskie";
    private static final String archivalURL="http://www.maciejowka.org/klepson/get_category_posts/?slug=archiwum";
    private static int publicationsNumber;
    private static JSONObject currentPublication;
    private static JSONObject archivalPublications;
    private static String[][] pubsDataArray;
    private static String SHARED_PREFERENCES_NAME="LOADER_SHARED_PREFERENCES";
    public static Context maciejAppActivityContext;
    public static PublicationsStatus publicationsStatus = PublicationsStatus.PENDING;
    public static ArrayList<Activity> activitiesToUpDate=new ArrayList<>();

    private enum DownloadingStatus {
        UPDATED, UP_TO_DATE, FAILURE
    }

    public enum PublicationsStatus {
        //RS - restored successfully, RF - restoring failed,
        // DS_US - downloaded successfully and updated, DS_UF - downloaded successfully but not updated, DF - downloading failed

        PENDING, RS, RF, RS_DS_US, RS_DS_UF, RS_DF, RF_DS_US, RF_DS_UF, RF_DF
    }

    public static PublicationsStatus loadPublications(){

        PublicationsStatus returnStatement= publicationsStatus;

        switch(publicationsStatus){
            case PENDING:{
                returnStatement = restorePubsDataArray()? PublicationsStatus.RS: PublicationsStatus.RF;
                break;
            }
            case RS: //go down
            case RS_DS_US: //go down
            case RS_DS_UF: //go down
            case RS_DF:{
                switch(downloadPubsToDataArray()){
                    case UP_TO_DATE:{
                        returnStatement = PublicationsStatus.RS_DS_UF;
                        break;
                    }
                    case UPDATED:{
                        returnStatement = PublicationsStatus.RS_DS_US;
                        break;
                    }
                    case FAILURE:{
                        returnStatement = PublicationsStatus.RS_DF;
                        break;
                    }
                }//switch
                break;
            }
            case RF: //go down
            case RF_DS_US: //go down
            case RF_DS_UF: //go down
            case RF_DF:{
                switch(downloadPubsToDataArray()){
                    case UP_TO_DATE:{
                        returnStatement = PublicationsStatus.RF_DS_UF;
                        break;
                    }
                    case UPDATED:{
                        returnStatement = PublicationsStatus.RF_DS_US;
                        break;
                    }
                    case FAILURE:{
                        returnStatement = PublicationsStatus.RF_DF;
                        break;
                    }
                }//switch
                break;
            }
        }//switch

        return returnStatement;
    }

    public static DownloadingStatus JSONObjectsToDataArray(){

        String out_of_date_title = ""; //used to check if there is a new publication

       try{
           try{ out_of_date_title = pubsDataArray[0][0]; }
           catch(Exception e) {e.printStackTrace();}

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
           return DownloadingStatus.FAILURE;
       }

       return out_of_date_title.equals(pubsDataArray[0][0])?DownloadingStatus.UP_TO_DATE:DownloadingStatus.UPDATED;
    }

    private static DownloadingStatus downloadPubsToDataArray(){
        loadPublicationsToJSONObjects();
        return JSONObjectsToDataArray();
    }

    private static boolean loadPublicationsToJSONObjects(){
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


    private static String getJSONfromURL(String url) throws Exception {
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

    private static void savePubsDataArray(){
        SharedPreferences sharedPref = maciejAppActivityContext.getSharedPreferences(SHARED_PREFERENCES_NAME, maciejAppActivityContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.putInt("publicationsNumber",publicationsNumber);
        for (int i = 0; i < publicationsNumber; i++){
            editor.putString("title"+i,pubsDataArray[i][0]);
            editor.putString("content"+i,pubsDataArray[i][1]);
        }
        editor.apply();

    }

    private static boolean restorePubsDataArray(){
        boolean success = true;
        SharedPreferences prefs = maciejAppActivityContext.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        publicationsNumber = prefs.getInt("publicationsNumber",0);
        pubsDataArray = new String[publicationsNumber][];
        if(publicationsNumber>0) {
            for (int i = 0; i < publicationsNumber&&success; i++){
                pubsDataArray[i] = new String[2];
                pubsDataArray[i][0] = prefs.getString("title"+i,"missing title");
                success = pubsDataArray[i][0] != "missing title";
                pubsDataArray[i][1] = prefs.getString("content"+i,"missing content");
                success = pubsDataArray[i][1] != "missing content";
            }
        }
        else{
            success=false;
        }

        return success;
    }

    public static int getpubsNumber(){
        return publicationsNumber;
    }

    public static String[][] getPubsDataArray() {
        return pubsDataArray;
    }

    @Override
    protected PublicationsStatus doInBackground(Void... params) {

        return loadPublications();
    }

    @Override
    protected void onPreExecute() {
        Log.d("MyLog","onPreExecute()");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(PublicationsStatus result) {

        publicationsStatus = result;
        upDateActivities();

        switch(result){
            case RS:{} //go down
            case RF:{
                new PublicationsLoader().execute();
                break;
            }
            case RS_DS_US:{} //go down
            case RF_DS_US:{
                Toast.makeText(maciejAppActivityContext, "Nowe ogłoszenia!", Toast.LENGTH_SHORT).show();
                savePubsDataArray();
                break;
            }
        }

    }

    private void upDateActivities(){

        for(int ii=0;ii<activitiesToUpDate.size();ii++){
            if(activitiesToUpDate.get(ii) instanceof SinglePublication){
                ((SinglePublication) activitiesToUpDate.get(ii)).upDatePublication();
            }
            else if(activitiesToUpDate.get(ii) instanceof ArchivalPublicationsList){
                ((ArchivalPublicationsList) activitiesToUpDate.get(ii)).upDateArchivalPublicationsList();
            }
        }//for

    }











}

