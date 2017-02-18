package com.example.root.maciejapp;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by maciej on 17.02.17.
 */
public class Publications extends AppCompatActivity{

    ListView listView;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;

    private enum PublicationsState{
        FAILED, LOADING, LOADED
    }
    private PublicationsState publicationsState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);
            listView = (ListView) findViewById(R.id.listView);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnPublicationClickListener());

            publicationsState = PublicationsState.LOADING;
            new ListViewCreator().execute();

    }

    private class OnPublicationClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (publicationsState) {
                case LOADING:
                    break;
                case FAILED: {
                    publicationsState = PublicationsState.LOADING;
                    new ListViewCreator().execute();
                    break;
                }
                case LOADED: {
                    Intent intent = new Intent(Publications.this, SinglePublication.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    break;
                }

            }//switch

        }//onItemClick(...)
    }

    private class ListViewCreator extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            return JSONOperator.completelyLoadPublications();
        }

        @Override
        protected void onPreExecute() {
            listItems.clear();
            listItems.add("Ładuję listę");
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                listItems.clear();
                for(int ii=0;ii<JSONOperator.getpubsNumber();ii++){
                    listItems.add(JSONOperator.getPubsDataArray()[ii][0]);
                }
                adapter.notifyDataSetChanged();
                publicationsState=PublicationsState.LOADED;
            }
            else {
                publicationsState=PublicationsState.FAILED;
                listItems.set(0,"Sprawdź połączenie i kliknij, aby spróbować ponownie");
                adapter.notifyDataSetChanged();
            }
        }
    }
}
