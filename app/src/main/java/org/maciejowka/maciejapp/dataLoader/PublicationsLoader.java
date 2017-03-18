package org.maciejowka.maciejapp.dataLoader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONObject;
import org.maciejowka.maciejapp.module.publications.ArchivalPublicationsList;
import org.maciejowka.maciejapp.module.publications.SinglePublication;

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


    private static final String currentURL="http://www.maciejowka.org/klepson/get_category_posts/?slug=ogloszenia-duszpasterskie";
    private static final String archivalURL="http://www.maciejowka.org/klepson/get_category_posts/?slug=archiwum";
    private static int publicationsNumber;
    private static JSONObject currentPublication;
    private static JSONObject archivalPublications;
    private static String[][] pubsDataArray;
    private static String SHARED_PREFERENCES_NAME="LOADER_SHARED_PREFERENCES";
    public static Context publicationsActivityContext;
    public static PublicationsStatus publicationsStatus = PublicationsStatus.PENDING;
    public static ArrayList<Activity> activitiesToUpDate=new ArrayList<>();

    private enum DownloadingStatus {
        NEW, UP_TO_DATE, FAILURE, MODIFIED
    }

    public enum PublicationsStatus {
        //RS - restored successfully, RF - restoring failed,
        // DS_NS - downloaded successfully with new publications
        // DF - downloading failed
        // DS_MS - downloaded successfully, without new publications, but with modifications
        // DS_MF - downloaded successfully, but without any changes

        PENDING, RS, RF, RS_DS_NS, RS_DS_MF, RS_DS_MS, RS_DF, RF_DS_NS, RF_DS_MS, RF_DS_MF, RF_DF
    }

    public static PublicationsStatus loadPublications(){

        PublicationsStatus returnStatement= publicationsStatus;

        switch(publicationsStatus){
            case PENDING:{
                returnStatement = restorePubsDataArray()? PublicationsStatus.RS: PublicationsStatus.RF;
                break;
            }
            case RS: //go down
            case RS_DS_NS: //go down
            case RS_DS_MF: //go down
            case RS_DS_MS: //go down
            case RS_DF:{
                switch(downloadPubsToDataArray()){
                    case UP_TO_DATE:{
                        returnStatement = PublicationsStatus.RS_DS_MF;
                        break;
                    }
                    case NEW:{
                        returnStatement = PublicationsStatus.RS_DS_NS;
                        break;
                    }
                    case MODIFIED:{
                        returnStatement = PublicationsStatus.RS_DS_MS;
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
            case RF_DS_NS: //go down
            case RF_DS_MF: //go down
            case RF_DS_MS: //go down
            case RF_DF:{
                switch(downloadPubsToDataArray()){
                    case UP_TO_DATE:{
                        returnStatement = PublicationsStatus.RF_DS_MF;
                        break;
                    }
                    case NEW:{
                        returnStatement = PublicationsStatus.RF_DS_NS;
                        break;
                    }
                    case MODIFIED:{
                        returnStatement = PublicationsStatus.RF_DS_MS;
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

        boolean pubsModified=false;
        boolean newPubs=false;
        DownloadingStatus returnDownloadingStatus;
        int iterPubStartIndex;

       try{

           int tempPublicationsNumber;
           String[][] tempPubsDataArray;
           iterPubStartIndex = 1;

           if(currentPublication.getInt("count")>0){

               try { //check for new publications titles
                   if (!currentPublication.getJSONArray("posts").getJSONObject(0).getString("title").equals(pubsDataArray[0][0]))
                       newPubs = true;
               }
               catch(Exception e){
                   e.printStackTrace();
                   newPubs=true;
               }

               tempPublicationsNumber = archivalPublications.getInt("count")+1; // +1 means current publication
               tempPubsDataArray = new String[tempPublicationsNumber][];

               tempPubsDataArray[0] = new String[3]; //3 because will contain title and content and modified Strings
               tempPubsDataArray[0][0] = currentPublication.getJSONArray("posts").getJSONObject(0).getString("title");
               tempPubsDataArray[0][1] = currentPublication.getJSONArray("posts").getJSONObject(0).getString("content");
               tempPubsDataArray[0][2] = currentPublication.getJSONArray("posts").getJSONObject(0).getString("modified");
               try {
                   if (!tempPubsDataArray[0][2].equals(pubsDataArray[0][2])) pubsModified = true;
               }
               catch(Exception e){e.printStackTrace(); pubsModified = true;}

           }
           else{ //missing current publication situation

               tempPublicationsNumber = archivalPublications.getInt("count");
               tempPubsDataArray = new String[tempPublicationsNumber][];
               iterPubStartIndex = 0;

           }

           for(int ii=iterPubStartIndex; ii<tempPublicationsNumber; ii++){
               tempPubsDataArray[ii]=new String[3]; //3 because will contain title and content and modified Strings
               tempPubsDataArray[ii][0] = archivalPublications.getJSONArray("posts").getJSONObject(ii-iterPubStartIndex).getString("title");
               tempPubsDataArray[ii][1] = archivalPublications.getJSONArray("posts").getJSONObject(ii-iterPubStartIndex).getString("content");
               tempPubsDataArray[ii][2] = archivalPublications.getJSONArray("posts").getJSONObject(ii-iterPubStartIndex).getString("modified");
               try {
                   if (!pubsModified && !tempPubsDataArray[ii][2].equals(pubsDataArray[ii][2])) pubsModified = true;
               }
               catch(Exception e){e.printStackTrace(); pubsModified = true;}
           }

           publicationsNumber = tempPublicationsNumber;
           pubsDataArray = tempPubsDataArray;
       }
       catch(Exception e){
           e.printStackTrace();
           return DownloadingStatus.FAILURE;
       }

       if(newPubs) returnDownloadingStatus = DownloadingStatus.NEW;
       else if(pubsModified) returnDownloadingStatus = DownloadingStatus.MODIFIED;
       else returnDownloadingStatus = DownloadingStatus.UP_TO_DATE;

       return returnDownloadingStatus;
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
        SharedPreferences sharedPref = publicationsActivityContext.getSharedPreferences(SHARED_PREFERENCES_NAME, publicationsActivityContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("publicationsNumber",publicationsNumber);



        for (int i = 0; i < publicationsNumber; i++){
            editor.putString("title"+i,pubsDataArray[i][0]);
            editor.putString("content"+i,pubsDataArray[i][1]);
            editor.putString("modified"+i,pubsDataArray[i][2]);
        }

        editor.apply();
    }

    private static boolean restorePubsDataArray(){
        boolean success = true;
        SharedPreferences prefs = publicationsActivityContext.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        publicationsNumber = prefs.getInt("publicationsNumber",0);
        pubsDataArray = new String[publicationsNumber][];
        if(publicationsNumber>0) {
            for (int i = 0; i < publicationsNumber&&success; i++){
                pubsDataArray[i] = new String[3];
                pubsDataArray[i][0] = prefs.getString("title"+i,"missing title");
                pubsDataArray[i][1] = prefs.getString("content"+i,"missing content");
                pubsDataArray[i][2] = prefs.getString("modified"+i,"missing modified");
                success = pubsDataArray[i][0] != "missing title" && pubsDataArray[i][1] != "missing content" && pubsDataArray[i][2] != "missing modified";
            }

        }
        else {
            success = false;
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
            case RS_DS_NS:{} //go down
            case RF_DS_NS:{
                Toast.makeText(publicationsActivityContext, "Nowe ogłoszenia!", Toast.LENGTH_SHORT).show();
                savePubsDataArray();
                break;
            }
            case RS_DS_MS:{} //go down
            case RF_DS_MS:{
                Toast.makeText(publicationsActivityContext, "Zmiany w ogłoszeniach", Toast.LENGTH_SHORT).show();
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

    public static void executeSelf(){
        new PublicationsLoader().executeOnExecutor(SERIAL_EXECUTOR);
    }
}
