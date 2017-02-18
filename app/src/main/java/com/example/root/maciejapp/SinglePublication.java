package com.example.root.maciejapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by maciej on 17.02.17.
 */
public class SinglePublication extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_publication);
        loadWebViewAndTitle();
    }



    private void loadWebViewAndTitle(){

        String[] publicationDataArray = new String[2];
        int position = getIntent().getIntExtra("position",0);

        publicationDataArray[0]=JSONOperator.getPubsDataArray()[position][0];
        publicationDataArray[1]=JSONOperator.getPubsDataArray()[position][1];


        Log.d("MyLog",publicationDataArray[0]+'\n'+publicationDataArray[1]);

        webView = (WebView)findViewById(R.id.webView);
        webView.loadData(publicationDataArray[1], "text/html; charset=utf-8", "UTF-8");

        setTitle(publicationDataArray[0]);
    }//loadWebView()








}//class
