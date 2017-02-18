package com.example.root.maciejapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MaciejApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maciej_app);
    }

    public void openPublications(View view){
        Intent intent = new Intent(MaciejApp.this, Publications.class);
        startActivity(intent);
    }
}
