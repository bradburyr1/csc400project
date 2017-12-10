package com.example.ryan.places;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.R.attr.value;
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
    static String state = "";
    static String forTimeShow = "";

    static String players = "";
    static String refs = "";

    static TextView timeShow;
    static TextView dateShow;

    static boolean inputCheck = false;//check whether the input is good

    String latitude = "";
    String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        timeShow = (TextView)findViewById(R.id.time_show);
        dateShow = (TextView)findViewById(R.id.date_show);


        final Spinner spinner = (Spinner) findViewById(R.id.comp_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.comp_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Button button = (Button) findViewById(R.id.Create);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newTok = "_";//The new token for those that need tokenizers

                //Sport
                EditText sportE = (EditText)findViewById(R.id.sport);
                String sport_content = sportE.getText().toString();
                sport = sport_content;
                StringTokenizer tokensSport = new StringTokenizer(sport, " ");
                String[] sportTokens = new String[tokensSport.countTokens()];
                sport = "";
                for(int i = 0; i < sportTokens.length - 1; i++){
                    sport += tokensSport.nextToken() + newTok;
                }
                sport += tokensSport.nextToken();
                //Log.d("ADDRESS@@@@@@@@@@@@@2", "#" + sport + "#" + sport_content);

                //City
                EditText cityE = (EditText)findViewById(R.id.city);
                String city_content = cityE.getText().toString();
                city = city_content;
                StringTokenizer tokensCity = new StringTokenizer(city, " ");
                String[] cityTokens = new String[tokensCity.countTokens()];
                city = "";
                for(int i = 0; i < cityTokens.length - 1; i++){
                    city += tokensCity.nextToken() + newTok;
                }
                city += tokensCity.nextToken();

                //Address
                EditText addressE = (EditText)findViewById(R.id.Postal);
                String postal_content = addressE.getText().toString();
                address = postal_content;
                StringTokenizer tokens = new StringTokenizer(address, " ");
                String[] addressTokens = new String[tokens.countTokens()];
                address = "";
                for(int i = 0; i < addressTokens.length - 1; i++){
                    address += tokens.nextToken() + newTok;
                }
                address += tokens.nextToken();

                //State
                EditText stateE = (EditText)findViewById(R.id.state);
                String state_content = stateE.getText().toString();

                //Spinner for competition level
                String spinner_content = spinner.getSelectedItem().toString();
                comp = spinner_content;

                //Maximum number of players
                EditText playersEdit = (EditText)findViewById(R.id.max_players_edit);
                String players_content = playersEdit.getText().toString();
                players = players_content;
                int intplayers = Integer.parseInt(players);

                //Maximum number of referees
                EditText refsEdit = (EditText)findViewById(R.id.max_refs_edit);
                String refs_content = refsEdit.getText().toString();
                refs = refs_content;
                int intrefs = Integer.parseInt(refs);

                if(intplayers <= 0){
                    Context context = getApplicationContext();
                    CharSequence text = "Number of players must be greater than 0.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(intrefs < 0){
                    Context context = getApplicationContext();
                    CharSequence text = "Number of referees cannot be less than 0.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Game created";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    findLatAndLong(postal_content, city_content, state_content);
                }
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

        //city
        StringTokenizer tokensCity = new StringTokenizer(city, " ");
        String[] cityTokens = new String[tokensCity.countTokens()];

        city = "";

        for(int i = 0; i < cityTokens.length; i++){
            if(i < cityTokens.length - 1){
                city += tokensCity.nextToken() + "+";
            }
            else{
                city += tokensCity.nextToken();
            }
        }

        //postal address
        StringTokenizer tokens = new StringTokenizer(postal, " ");
        /*String houseNum = tokens.nextToken();
        String streetName = tokens.nextToken();
        String streetSuffix = tokens.nextToken();*/
        String[] addressTokens = new String[tokens.countTokens()];

        String glaladdress = "";

        for(int i = 0; i < addressTokens.length; i++){
            if(i < addressTokens.length - 1){
                glaladdress += tokens.nextToken() + "+";
            }
            else{
                glaladdress += tokens.nextToken();
            }
        }

        Log.d("ADDRESS%%%%%%%%%5", "#" + glaladdress);

        GetLatAndLong glal = new GetLatAndLong();
        glal.address = glaladdress;
        /*glal.house = houseNum;
        glal.street = streetName;
        glal.streetSuf = streetSuffix;*/
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
            //Toast.makeText(getApplicationContext(), "There seems to be a problem with the address you entered",
              //      Toast.LENGTH_SHORT).show();
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
        mg.players = players;
        mg.refs = refs;
        mg.starter();
    }
/////////////////////////////////////////Time Picker
    public void timeDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String amOrPm = "";//will be assigned a string value
            if(hourOfDay == 0){
                hourOfDay = 12;
                amOrPm = "AM";
            }
            else if(hourOfDay >= 12){
                if(hourOfDay == 12){
                    amOrPm = "PM";
                }
                else {
                    hourOfDay -= 12;
                    amOrPm = "PM";
                }
            }
            else{
                amOrPm = "AM";
            }

            String minuteFix = "";
            if(minute != 0){
                minuteFix = String.valueOf(minute);//In this case, no need to change anything
            }
            else {
                minuteFix = "00";//Minute shows up with only one zero, such as "6:0". This will fix it
            }

            time = hourOfDay + "_" + minuteFix + "_" + amOrPm;
            forTimeShow = hourOfDay + ":" + minuteFix + " " + amOrPm;

            timeShow.setText(forTimeShow);

            Log.d("TIME(((((((((((((", "#" + time);
        }
    }
    ////////////////////////////////////////
    /////////////////////////////////////////Date Picker
    public void dateDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            // Create a new instance of DatePickerDialog and return it
            return dpd;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month ++;
            date = month + "/" + day + "/" + year;
            dateShow.setText(date);
        }
    }
}