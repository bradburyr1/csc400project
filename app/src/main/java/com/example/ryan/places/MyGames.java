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

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

import static com.example.ryan.places.MapsActivity.j;

/*
This activity is the list view that displays a user's owned games, the games they're signed up for, or it can be used as a replacement for
the map view, accessible from a button on the map.
 */

public class MyGames extends AppCompatActivity {

    public static Context context;

    public static HttpContext localContext;

    public static double[] longitude;
    public static double[] latitude;
    public static String[] title;
    public static String[] game_id;
    public static String[] city;
    public static String[] time;
    public static String[] date;
    public static String[] comp_level;
    public static String[] postalAddress;
    public static String[] user_id;
    public static String[] max_players;
    public static String[] max_refs;
    public static String[] curr_players;
    public static String[] curr_refs;
    public static String uid;


    public static String result = "";

    GameInfo gi = new GameInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MG", "The LocalContext GA: " + localContext);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);

        context = getApplicationContext();

        //Toolbar is the top bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start of the layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        //Here we loop through the number of games, making a button for each one
        Log.d("Number of games: ", "bbbbb - " + game_id.length);
        for (int j = 0; j < game_id.length; j++) {

            Log.d("Title: ", "bbbbb - " + title[j]);

            final int i = j;//Need to have a final variable for onclick to use, since the loop iteration
            //number (j) is being accessed from an inner class. So, assign a final (i) to j in each iteration.
            Button game = new Button(this);
            game.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            //////////////Fix the information
            //Sport
            String[] newSportArr = redo(title[j]);
            String newTitle = "";
            int otherK = 0;
            for(int k = 0; k < newSportArr.length - 1; k++){
                newTitle += newSportArr[k] + " ";
                otherK = k + 1;
            }
            newTitle += newSportArr[otherK];//Trying to get rid of that extra space
            //City
            String[] newCityArr = redo(city[j]);
            String newCity = "";
            for(int k = 0; k < newCityArr.length; k++){
                newCity += newCityArr[k] + " ";
            }
            //////////////
            game.setText(newTitle + " on " + date[j] + " in " + newCity);//What appears on the button is changeable here
            game.setId(j + 1);
            game.setOnClickListener(new View.OnClickListener() {//Now what happens when the button is clicked
                public void onClick(View v) {
                    gi.localContext = localContext;
                    //Log.d("MG", "The LocalContext GA: " + localContext + ", " + gi.localContext);
                    gi.title = title[i];
                    gi.game_id = game_id[i];
                    gi.city = city[i];
                    gi.time = time[i];
                    gi.date = date[i];
                    gi.comp_level = comp_level[i];
                    gi.postalAddress = postalAddress[i];
                    gi.user_id = user_id[i];
                    gi.max_players = max_players[i];
                    gi.max_refs = max_refs[i];
                    gi.curr_players = curr_players[i];
                    gi.curr_refs = curr_refs[i];
                    startActivity(new Intent(MyGames.this, GameInfo.class));
                }
            });

            //Hide games with past dates
            String[] newDateArr = redoDate(date[j]);
            int month = Integer.parseInt(newDateArr[0]);
            int day = Integer.parseInt(newDateArr[1]);
            int year = Integer.parseInt(newDateArr[2]);
            boolean inFutute = true;//flag which will become false if the date is in the future

            final Calendar c = Calendar.getInstance();
            int currday = c.get(Calendar.DAY_OF_MONTH);
            int curryear = c.get(Calendar.YEAR);
            int currmonth = c.get(Calendar.MONTH);
            currmonth++;

            Log.d("dddddd", "Years: " + year + ", " + curryear);
            Log.d("dddddd", "Months: " + month + ", " + currmonth);
            Log.d("dddddd", "Days: " + day + ", " + currday);

            if(year > curryear){//If year is greater, skip everything else
                inFutute = true;
            }
            else if(year == curryear) {//If year is equal, check further
                if (month > currmonth) {//if month is greater and year is equal, stop here
                    inFutute = true;
                }
                else if (month == currmonth) {//check further if month and year are both equal
                    if (day > currday) {//
                        inFutute = true;
                    }
                    else if (day != currday){
                        inFutute = false;
                    }
                }
                else{
                    inFutute = false;
                }
            }
            else{
                inFutute = false;
            }

            if(inFutute){
                layout.addView(game);
            }

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

        //Log.d("Response from server: ", "in parseJSON" + result);

        try {
            JSONObject jsonParse = new JSONObject(res);//create a json object
            JSONArray markers  = jsonParse.getJSONArray("markers");//use 'markers' to create an array of the points 'markers' has
            //now we know how many points there are, we can finish making the latitude and longitude arrays
            longitude = new double[markers.length()];
            latitude = new double[markers.length()];
            title = new String[markers.length()];
            city = new String[markers.length()];
            time = new String[markers.length()];
            game_id = new String[markers.length()];
            date = new String[markers.length()];
            comp_level = new String[markers.length()];
            postalAddress = new String[markers.length()];
            user_id = new String[markers.length()];
            max_players = new String[markers.length()];
            max_refs = new String[markers.length()];
            curr_players = new String[markers.length()];
            curr_refs = new String[markers.length()];

            for(int i = 0; i < markers.length(); i++) {//loop to  work on each entry from "markers" individually
                //turn the current json entry from markers (which will be individual points) into objects

                JSONObject point = markers.getJSONObject(i);

                //assign the values into the appropriate spots in each array
                longitude[i] = point.getDouble("longitude");
                latitude[i] = point.getDouble("latitude");
                title[i] = point.getString("title");
                game_id[i] = point.getString("gameid");
                city[i] = point.getString("city");
                time[i] = point.getString("time");
                date[i] = point.getString("date");
                comp_level[i] = point.getString("comp_level");
                postalAddress[i] = point.getString("postalAddress");
                user_id[i] = point.getString("user_id");
                max_players[i] = point.getString("max_signed");
                max_refs[i] = point.getString("max_refs");
                curr_players[i] = point.getString("curr_signed");
                curr_refs[i] = point.getString("curr_refs");

                Log.d("Latitude^^^^^^^^^^^", "Latitude: " + latitude[i]);
                Log.d("Longitude^^^^^^^^^^^", "Longitude: " + longitude[i]);
                Log.d("Title^^^^^^^^^^^", "Title: " + title[i]);
                Log.d("postalAddress^^^^^^^^^^", "postalAddress: " + postalAddress[i]);
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
    public String[] redoDate(String old){//Just like redo, except just for date
        StringTokenizer tokensNew = new StringTokenizer(old, "/");
        String[] newStr = new String[tokensNew.countTokens()];
        Log.d("Count((((((((", "num: " + tokensNew.countTokens());
        for(int i = 0; i < newStr.length; i++){
            newStr[i] = tokensNew.nextToken();
            Log.d("New String(((((((", "newStr " + i + ": " + newStr[i]);
        }
        return newStr;
    }
}
