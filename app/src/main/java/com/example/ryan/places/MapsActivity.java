package com.example.ryan.places;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.support.v7.widget.AppCompatDrawableManager.get;
import static com.example.ryan.places.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    int check = 1;

    public static boolean jsonDone = false;

    GetAllMarkers gam = new GetAllMarkers();

    //These variables need to be static so they can be worked on by multiple methods
    public static double[] longitude;
    public static double[] latitude;
    public static String[] title;
    SupportMapFragment mapFragment;
    SupportMapFragment mapFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //newOnCreate();
        /*if (savedInstanceState != null) {
            //Restore the fragment's instance
            mapFragment = (SupportMapFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "mapFragment");
        }*/

        gam.starter();
        String result = gam.get();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mapFragment", mapFragment);


    }*/


    /*private void newOnCreate(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(jsonDone == false){
        gam.starter();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
            mapFragment2 = mapFragment;}
        else if(jsonDone == true){
            mapFragment2.getMapAsync(this);
        }
    }*/

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

        check = 2;
        if(mMap != null){
            Log.d("Nope, --------", "It's not null in here." + check);
        }

        if(jsonDone == false){
            Log.d("Nope, --------", "JSON IS NOT DONE." + latitude[0]);
        }
        else if(jsonDone == true){
            Log.d("Yep, --------", "JSON IS DONE." + latitude[0]);
        }
        if (jsonDone == true) {
            for (int i = 0; i < longitude.length; i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude[i], longitude[i]))
                        .title(title[i])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        }

        //float lng = getLong();

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        /*mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/



    }
    public void getLong (String res)throws IOException {
        //String longitude = "";
        Log.d("MAPS^^^^^^ACTIVITY", "test: " + res);//gam: GetAllMarkers, rst: json string result
        //return longitude;

        //sample json string for testing off wifi:
        String jsonString = "{\"markers\":[{\"latitude\":\"-34\",\"longitude\":\"151\",\"title\":\"Sydney\"},{\"latitude\"" +
                ":\"43\",\"longitude\":\"-77\",\"title\":\"Northeast US\"}],\"success\":1}";

        try {
            //Log.d("Before+++++++++", "before");
            JSONObject jsonParse = new JSONObject(res);//create a json object
            //Log.d("After+++++++++", "after");
            JSONArray markers  = jsonParse.getJSONArray("markers");//use 'markers' to create an array of the
                                                                    //points 'markers' has
            //now we know how many points there are, we can finish making the latitude and longitude arrays
            longitude = new double[markers.length()];
            latitude = new double[markers.length()];
            title = new String[markers.length()];

            //Log.d("After2+++++++++", "after");
            for(int i = 0; i < markers.length(); i++) {//loop to  work on each entry from "markers" individually
                //turn the current json entry from markers (which will be individual points) into objects
                JSONObject point = markers.getJSONObject(i);
                //Log.d("After2.5+++++++++", "after");

                //assign the values into the appropriate spots in each array
                longitude[i] = point.getDouble("longitude");
                latitude[i] = point.getDouble("latitude");
                title[i] = point.getString("title");

                Log.d("Latitude^^^^^^^^^^^", "Latitude: " + latitude[i]);
                Log.d("Longitude^^^^^^^^^^^", "Longitude: " + longitude[i]);
                Log.d("Title^^^^^^^^^^^", "Title: " + title[i]);
            }
            Log.d("After loop=========", "THIS IS JUST AFTER THE LOOP." + check);
            //Log.d("After3+++++++++", "after");

        }
        catch(JSONException j){
            Log.d("JSON******EXCEPTION", "Exception: " + j);
        }
        jsonDone = true;
        //newOnCreate();
        /*mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
        if(mMap == null){
            Log.d("Yup, --------", "It's null." + check);
        }

        for(int i = 0; i < longitude.length; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude[i], longitude[i]))
                    .title(title[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        }
        });*/
    }
}
