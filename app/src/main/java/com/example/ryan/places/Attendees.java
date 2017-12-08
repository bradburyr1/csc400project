package com.example.ryan.places;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

public class Attendees extends AppCompatActivity {

    public static Context context;

    public static String[] user_id;
    public static String[] role;


    public static String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);

        context = getApplicationContext();

        //Toolbar is the top bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start of the layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        //Here we loop through the number of games, making a button for each one
        for (int j = 0; j < user_id.length; j++) {
            final int i = j;//Need to have a final variable for onclick to use, since the loop iteration
            //number (j) is being accessed from an inner class. So, assign a final (i) to j in each iteration.
            TextView userText = new TextView(this);
            userText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            //////////////Fix the information

            //City
            //Owner
            String[] newUserArr = redo(user_id[j]);
            String newOwn = newUserArr[0];
            try{//Some of the earlier ones aren't formatted correctly
                newOwn += "@" + newUserArr[1];
            }
            catch (ArrayIndexOutOfBoundsException a){
                Log.d("Owner array OOB(((((((", "Exception: " + a);
            }
            try{//I didn't find out until later that I would need to replace the dot too in the email address.
                newOwn += "." + newUserArr[2];
            }
            catch (ArrayIndexOutOfBoundsException a){
                Log.d("Owner array OOB(((((((", "Exception: " + a);
            }
            //////////////
            userText.setText(newOwn + ", " + role[j]);//What appears on the button is changeable here
            userText.setId(j + 1);
            layout.addView(userText);
        }

        //Easiest way to make the floating action button go away is to program it in and set its visibility to "GONE"
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void parseJSON ()throws IOException {

        String res = result;
        Log.d("MAPS^^^^^^ACTIVITY", "test: " + res);//gam: GetAllMarkers, rst: json string result

        try {
            JSONObject jsonParse = new JSONObject(res);//create a json object
            JSONArray markers  = jsonParse.getJSONArray("markers");//use 'markers' to create an array of the points 'markers' has
            //now we know how many points there are, we can finish making the latitude and longitude arrays
            user_id = new String[markers.length()];
            role = new String[markers.length()];

            for(int i = 0; i < markers.length(); i++) {//loop to  work on each entry from "markers" individually
                //turn the current json entry from markers (which will be individual points) into objects

                JSONObject point = markers.getJSONObject(i);

                //assign the values into the appropriate spots in each array
                user_id[i] = point.getString("user_id");
                role[i] = point.getString("role");
            }
        }
        catch(JSONException j){
            Log.d("JSON******EXCEPTION", "Exception: " + j);
        }
    }

    public String[] redo(String old){//A method for the part that is the same for each one that needs fixing
        //In case I want it later it'll be here
        StringTokenizer tokensNew = new StringTokenizer(old, "_");
        String[] newStr = new String[tokensNew.countTokens()];
        Log.d("Count((((((((", "num: " + tokensNew.countTokens());
        for(int i = 0; i < newStr.length; i++){
            newStr[i] = tokensNew.nextToken();
            Log.d("New String(((((((", "newStr " + i + ": " + newStr[i]);
        }
        return newStr;
    }
}
