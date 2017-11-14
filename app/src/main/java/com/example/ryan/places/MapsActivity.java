package com.example.ryan.places;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

import static com.example.ryan.places.R.id.map;
import static java.security.AccessController.getContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static boolean jsonDone = false;
    //GetAllMarkers gam = new GetAllMarkers();

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

    static int j = 0;

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
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude[i], longitude[i]))
                        .title(game_id[i])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                j = i;
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < longitude.length; i++) {
                    if(game_id[i].equals(marker.getTitle())){
                    Log.d("clickedTitle@@@@@", "clickedTitle: " + postalAddress[i] + ", city: " + city[i]);
                    }
                }
                return false;
            }
        });
    }

    public void parseJSON ()throws IOException {
        //sample json string for testing off wifi:
        /*String jsonString = "{\"markers\":[{\"latitude\":\"-34\",\"longitude\":\"151\",\"title\":\"Sydney\"},{\"latitude\"" +
                ":\"43\",\"longitude\":\"-77\",\"title\":\"Northeast US\"}],\"success\":1}";*/

        String res = result;
        Log.d("MAPS^^^^^^ACTIVITY", "test: " + res);//gam: GetAllMarkers, rst: json string result


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
}