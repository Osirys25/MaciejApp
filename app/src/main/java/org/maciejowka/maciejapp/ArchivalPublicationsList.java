package org.maciejowka.maciejapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by maciej on 19.02.17.
 */

public class ArchivalPublicationsList extends AppCompatActivity{
    private ListView listView;
    private ArrayList<String> listItems=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archival_publications);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        hideRefreshButton();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ArchivalPublicationsList.OnPublicationClickListener());
        PublicationsLoader.activitiesToUpDate.add(this); //handler to this activity, allows PublicationsLoader to upDate this Activity data
        upDateArchivalPublicationsList();
    }

    private class OnPublicationClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(ArchivalPublicationsList.this, SinglePublication.class);
            intent.putExtra("current", false); //powiadamiam aktywność SinglePublication, że ma wyświetlić archiwalne ogłoszenia
            intent.putExtra("position", position);
            startActivity(intent);

        }//onItemClick(...)
    }

    public void upDateArchivalPublicationsList(){

        switch(PublicationsLoader.publicationsStatus){
            case RS_DS_NS: //go down
            case RF_DS_NS: //go down
            case RS_DS_MF: //go down
            case RF_DS_MF: //go down
            case RS_DS_MS: //go down
            case RF_DS_MS: //go down
            case RS_DF: //go down
            case RS:{
                hideRefreshButton();
                listItems.clear();
                for (int ii = 1; ii < PublicationsLoader.getpubsNumber(); ii++) {
                    listItems.add(PublicationsLoader.getPubsDataArray()[ii][0]);
                }
                adapter.notifyDataSetChanged();
                break;
            }
            case RF_DF:{
                showRefreshButton();
                break;
            }
        }//switch

    }

    public void showRefreshButton(){
        refreshButton.setVisibility(View.VISIBLE);
    }

    public void hideRefreshButton(){
        refreshButton.setVisibility(View.GONE);
    }

    public void refresh(View view){
        PublicationsLoader.executeSelf();
    }

}
