package org.maciejowka.maciejapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by maciej on 17.02.17.
 */

public class SinglePublication extends AppCompatActivity {

    private WebView webView;
    private String[] publicationDataArray;
    private Button refreshButton;
    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_publication);
        PublicationsLoader.activitiesToUpDate.add(this); //handler to this activity, allows PublicationsLoader to upDate this Activity data
        refreshButton = (Button) findViewById(R.id.refreshButton);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        upDatePublication();
    }


    private void loadPublicationData(){

        publicationDataArray = new String[2];

        switch(PublicationsLoader.publicationsStatus){
            case RS_DS_NS: //go down
            case RF_DS_NS: //go down
            case RF_DS_MF: //go down
            case RS_DS_MF: //go down
            case RS_DS_MS: //go down
            case RF_DS_MS: //go down
            case RS_DF: //go down
            case RS:{
                int position = getIntent().getIntExtra("position", 0);
                boolean isCurrent = getIntent().getBooleanExtra("current", true);

                if (isCurrent) {
                    showRefreshButton();
                    publicationDataArray[0] = PublicationsLoader.getPubsDataArray()[0][0];
                    publicationDataArray[1] = PublicationsLoader.getPubsDataArray()[0][1];
                }
                else{
                    hideRefreshButton();
                    position += 1; //poprawka na pozycję archiwalnych ogłoszeń
                    publicationDataArray[0] = PublicationsLoader.getPubsDataArray()[position][0];
                    publicationDataArray[1] = PublicationsLoader.getPubsDataArray()[position][1];
                }
                break;
            }
            case RF_DF:{
                showRefreshButton();
                publicationDataArray[0] = "Aktualne ogłoszenia";
                publicationDataArray[1] = "";
                break;
            }
        }//switch

        //przyda się Log.d("MyLog",publicationDataArray[0]+'\n'+publicationDataArray[1]);
    }

    private void loadWebView(){

        webView = (WebView)findViewById(R.id.webView);
        webView.loadDataWithBaseURL("file:///android_asset/", publicationDataArray[1], "text/html; charset=utf-8", "UTF-8", null); //webView.loadData nie obsługuje arkuszy CSS

    }//loadWebView()

    private void loadTitle(){
        setTitle(publicationDataArray[0]);
    }

    public void upDatePublication(){
        loadPublicationData();
        loadTitle();
        loadWebView();
    }

    public void showRefreshButton(){
        refreshButton.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);
    }

    public void hideRefreshButton(){
        refreshButton.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
    }

    public void refresh(View view){
            PublicationsLoader.executeSelf();
    }




}//class
