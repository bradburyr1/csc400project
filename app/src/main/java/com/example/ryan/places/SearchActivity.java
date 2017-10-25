package com.example.ryan.places;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;

import static com.example.ryan.places.R.id.sport;

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

                EditText sport = (EditText)findViewById(R.id.sport);
                String sport_content = sport.getText().toString();

                EditText city = (EditText)findViewById(R.id.city);
                String city_content = city.getText().toString();

                CheckBox comp = (CheckBox)findViewById(R.id.competitive);
                boolean comp_bool = comp.isChecked();
                CheckBox fun = (CheckBox)findViewById(R.id.fun);
                boolean fun_bool = fun.isChecked();

                Log.d("#####HOWDY######", "FUN: " + fun_bool + ", COMP: " + comp_bool);

                //send all of the user's search terms to marker search
                ms.title = sport_content;
                ms.city = city_content;
                ms.comp = comp_bool;
                ms.fun = fun_bool;

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
