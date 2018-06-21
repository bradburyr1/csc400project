package com.example.ryan.places;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static android.os.Build.VERSION_CODES.M;
import static com.example.ryan.places.GameInfo.view;
import static com.example.ryan.places.R.id.map;
import static java.security.AccessController.getContext;

/*
This is the activity for the map that displays search results
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static boolean jsonDone = false;

    public static HttpContext localContext;

    MyGames mg = new MyGames();

    //These variables need to be static so they can be worked on by multiple methods
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

    static int j = 0;

    GameInfo gi = new GameInfo();

    public static String result = "";

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, SearchActivity.class));
            }
        });

        //gam.starter();
        //gam.pauseMarkers();//Make sure the JSON can finish parsing
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        final Button chView = (Button) findViewById(R.id.list);//Change view to a list
        chView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login l = new Login();
                GameInfo gi = new GameInfo();
                gi.view = 0;
                gi.uid = uid;
                l.acceptRes(result, localContext);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (jsonDone == true) {
            for (int i = 0; i < longitude.length; i++) {

                //Hide markers for old games
                String[] newDateArr = redoDate(date[i]);
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
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude[i], longitude[i]))
                            .title(game_id[i])
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
                j = i;
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < longitude.length; i++) {
                    if(game_id[i].equals(marker.getTitle())){
                    //Log.d("clickedTitle@@@@@", "clickedTitle: " + postalAddress[i] + ", city: " + city[i]);

                        //Set all the variables for the GameInfo activity
                        gi.localContext = localContext;

                        gi.title = title[i];
                        gi.game_id = game_id[i];
                        gi.city = city[i];
                        gi.time = time[i];
                        gi.date = date[i];
                        gi.comp_level = comp_level[i];
                        gi.postalAddress = postalAddress[i];
                        gi.user_id = user_id[i];
                        gi.uid = uid;
                        gi.max_players = max_players[i];
                        gi.max_refs = max_refs[i];
                        gi.curr_players = curr_players[i];
                        gi.curr_refs = curr_refs[i];
                    }
                }
                view = 0;
                startActivity(new Intent(MapsActivity.this, GameInfo.class));
                return false;
            }
        });
    }

    public void parseJSON ()throws IOException {

        String res = result;
        Log.d("MAPS^^^^^^ACTIVITY", "test: " + res);


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

                ///////////////////////////////////////

                /*Log.d("Latitude^^^^^^^^^^^", "Latitude: " + latitude[i]);
                Log.d("Longitude^^^^^^^^^^^", "Longitude: " + longitude[i]);
                Log.d("Title^^^^^^^^^^^", "Title: " + title[i]);
                Log.d("postalAddress@@@@@", "postalAddress: " + postalAddress[i]);*/
            }
        }
        catch(JSONException j){
            Log.d("JSON******EXCEPTION", "Exception: " + j);
        }
        jsonDone = true;
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