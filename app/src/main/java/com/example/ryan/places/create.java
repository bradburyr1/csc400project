package com.example.ryan.places;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.StringTokenizer;

import static com.example.ryan.places.R.id.add;
import static com.example.ryan.places.R.id.city;

public class create extends AppCompatActivity {

    MakeGame mg = new MakeGame();

    static String sport = "";
    static String time = "";
    static String address = "";
    static String date = "";
    static String city = "";
    static String comp = "";
    static String uid = "";

    String latitude = "";
    String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final Spinner spinner = (Spinner) findViewById(R.id.comp_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.comp_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Button button = (Button) findViewById(R.id.Create);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newTok = "_";//The new token for those that need tokenizers

                EditText sportE = (EditText)findViewById(R.id.sport);
                String sport_content = sportE.getText().toString();
                sport = sport_content;
                //Log.d("ADDRESS@@@@@@@@@@@@@2", "#" + sport + "#" + sport_content);

                EditText cityE = (EditText)findViewById(R.id.city);
                String city_content = cityE.getText().toString();
                city = city_content;

                EditText addressE = (EditText)findViewById(R.id.Postal);
                String postal_content = addressE.getText().toString();
                address = postal_content;

                //Obviously we can't send the address through with spaces
                StringTokenizer tokens = new StringTokenizer(address, " ");
                String houseNum = tokens.nextToken();
                String streetName = tokens.nextToken();
                String streetSuffix = tokens.nextToken();
                address = houseNum + newTok + streetName + newTok + streetSuffix;

                EditText timeE = (EditText)findViewById(R.id.time);
                String time_content = timeE.getText().toString();
                time = time_content;

                StringTokenizer tokenTime = new StringTokenizer(time, ":");
                String hour = tokenTime.nextToken();
                String minute = tokenTime.nextToken();
                time = hour + newTok + minute;

                EditText dateE = (EditText)findViewById(R.id.date);
                String date_content = dateE.getText().toString();
                date = date_content;

                EditText stateE = (EditText)findViewById(R.id.state);
                String state_content = stateE.getText().toString();

                String spinner_content = spinner.getSelectedItem().toString();
                comp = spinner_content;

                findLatAndLong(postal_content, city_content, state_content);
            }
        });
    }

    /*
    This method gets the user's inputted address and gives it to a Google API to find information,
    including what we want, latitude and longitude, based on the address.
     */
    public void findLatAndLong(String postal, String city, String state) {
        //Geocoding API Key:
        String api_key = "AIzaSyA_RwGjahNWZ-gSpvS-B1lhAoCIYwzsFS4";

        StringTokenizer tokens = new StringTokenizer(postal, " ");
        String houseNum = tokens.nextToken();
        String streetName = tokens.nextToken();
        String streetSuffix = tokens.nextToken();

        GetLatAndLong glal = new GetLatAndLong();
        glal.house = houseNum;
        glal.street = streetName;
        glal.streetSuf = streetSuffix;
        glal.city = city;
        glal.state = state;
        glal.key = api_key;
        glal.starter();

        //Log.d("ADDRESS@@@@@@@@@@@@@2", "#" + houseNum + "#" + streetName + "#" + streetSuffix + "#" + city + "#" + state);
    }

    /*
    Parse the JSON string for getting latitude and longitude from an address
     */
    public void parseForLatAndLong(String result)throws IOException {
        try{
        JSONObject jsonParse = new JSONObject(result);//create a json object
            //Now use the JSON object to parse for latitude and longitude
            latitude = jsonParse.getJSONArray("results").getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            longitude = jsonParse.getJSONArray("results").getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

        }
        catch(JSONException j){
            Log.d("JSON******EXCEPTION", "Exception: " + j);
        }
        //Log.d("ADDRESS@@@@@@@@@@@@@2", "#" + latitude + "#" + longitude);

        mg.sport = sport;
        Log.d("ADDRESS@@@@@@@@@@@@@2", "#" + sport + "#" + mg.sport);
        mg.city = city;
        mg.time = time;
        mg.address = address;
        mg.date = date;
        mg.comp = comp;
        mg.uid = uid;
        mg.lat = latitude;
        mg.lng = longitude;
        mg.starter();
    }
}