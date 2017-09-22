package org.maciejowka.publications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import org.maciejowka.R;

/**
 * Created by maciej on 17.02.17.
 */

public class SinglePublication extends AppCompatActivity {

    private WebView webView;
    private String[] publicationDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_publication);
        PublicationsLoader.activitiesToUpDate.add(this); //handler to this activity, allows PublicationsLoader to upDate this Activity data

        Toolbar toolbar = findViewById(R.id.toolbar_single_publication);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PublicationsLoader.singleActivityContext = this;
        new PublicationsLoader().executeSelf();
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

                    publicationDataArray[0] = PublicationsLoader.getPubsDataArray()[0][0];
                    publicationDataArray[1] = PublicationsLoader.getPubsDataArray()[0][1];


                //CSS metadata

                if(!PublicationHTML.findMetadata(publicationDataArray[1]))
                    publicationDataArray[1] = PublicationHTML.addMetadata(publicationDataArray[1]);     //dodanie metadanych do obsługi CSS (jeśli nie zostały dodane wcześniej)

                else
                    if(!PublicationHTML.findStylesheet(publicationDataArray[1]))
                        publicationDataArray[1] = PublicationHTML.replaceStylesheet(publicationDataArray[1]);   //podmiana arkusza CSS

                //Log.d("CSS check: ", publicationDataArray[1]);

                break;
            }
            case RF_DF:{
                publicationDataArray[0] = "Aktualne ogłoszenia";
                publicationDataArray[1] = "";
                break;
            }
        }
    }

    private void loadWebView(){
        webView = findViewById(R.id.web_view);
        webView.loadDataWithBaseURL("file:///android_asset/", publicationDataArray[1], "text/html; charset=utf-8", "UTF-8", null);
    }

    private void loadTitle(){
        setTitle(publicationDataArray[0]);
    }

    public void upDatePublication(){
        loadPublicationData();
        loadTitle();
        loadWebView();
    }

    public void refresh(View view) {
        PublicationsLoader.executeSelf();
    }

    private static class PublicationHTML {

        static final String HTML_METADATA_0 = "<!DOCTYPE html><html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"";
        static final String HTML_METADATA_1 = "\"></head><body>";
        static final String HTML_METADATA_2 = "</body></html>";
        static final String STYLESHEET = "notices/style.css";

        static String addMetadata(String publication) {
            return new StringBuilder().insert(0, publication).insert(0, HTML_METADATA_1).insert(0, STYLESHEET).insert(0, HTML_METADATA_0).append(HTML_METADATA_2).toString();
        }

        static boolean findMetadata(String publication) {
            return (publication.contains("<!DOCTYPE html>"));
        }

        static boolean findStylesheet(String publication) {
            return getStylesheet(publication).equals(STYLESHEET);
        }

        static String getStylesheet(String publication) {
            return new StringBuilder().append(publication.substring(publication.indexOf("href=\"") + 6, publication.indexOf("\"", publication.indexOf("href=\"") + 6))).toString();
        }

        static String replaceStylesheet(String publication) {
            int index = publication.indexOf(getStylesheet(publication));
            int length = getStylesheet(publication).length();

            return new StringBuilder(publication).delete(index, index + length).insert(index, STYLESHEET).toString();
        }
    }
}
