package com.example.ryan.places;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity {

    Login login = new Login();

    MarkerSearch ms = new MarkerSearch();
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = getApplicationContext();

        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ms.starter();
            }
        });
        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, Login.class));
            }
        });
    }

    public void acceptRes(String result){
        MapsActivity ma = new MapsActivity();
        ma.result = result;

        try{
        ma.parseJSON();
        }
        catch(IOException e)
        {
            Log.d("IO******EXCEPTION", "(acceptRes) Exception: " + e);
        }

        Intent i = new Intent(context, MapsActivity.class);
        context.startActivity(i);
    }
}
