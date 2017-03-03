package com.example.root.maciejapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MaciejApp extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maciej_app);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,new String[]{"Aktualne ogłoszenia","Archiwalne ogłoszenia"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new MaciejApp.OnMaciejAppItemClickListener());
        PublicationsLoader.maciejAppActivityContext = this;
        new PublicationsLoader().execute();
        Log.d("MyLog","onCreate()");
    }

    private class OnMaciejAppItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (position) {
                case 0: {
                    Intent intent = new Intent(MaciejApp.this, SinglePublication.class);
                    intent.putExtra("current", true); //powiadamiam aktywność SinglePublication, że ma wyświetlić bieżące ogłoszenia
                    startActivity(intent);
                    break;
                }
                case 1:{
                    Intent intent = new Intent(MaciejApp.this, ArchivalPublicationsList.class);
                    startActivity(intent);
                    break;
                }

            }//switch

        }//onItemClick(...)
    }








}

// Klepa tu byl
//i znowu na tym samym branchu