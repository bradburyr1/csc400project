package com.example.ryan.places;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.StringTokenizer;

import static android.R.attr.button;

public class GameInfo extends AppCompatActivity {

    signup su = new signup();

    public static String title = "";
    public static String game_id = "";
    public static String city = "";
    public static String time = "";
    public static String date = "";
    public static String comp_level = "";
    public static String postalAddress = "";
    public static String user_id = "";//Owner
    public static String uid = "";//User viewing

    static int view = 0;//0: from map
                        //1: games signed up for
                        //2: games owned

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        //This is where all the textviews get their information set to them
        TextView sport = (TextView)findViewById(R.id.sport);
        sport.setText("Sport: " + title);

        TextView citytext = (TextView)findViewById(R.id.city);
        String[] newCityArr = redo(city);
        String newCity = "";
        for(int i = 0; i < newCityArr.length; i++){
            newCity += newCityArr[i] + " ";
        }
        citytext.setText("City: " + newCity);

        TextView timetext = (TextView)findViewById(R.id.time);
        Log.d("Time Old Text(((((((", "Old Text: " + time);
        String[] newTimeArr = redo(time);
        String newTime = newTimeArr[0];
        try{//Some of the test games don't have the time formatted correctly (early example games)
            newTime += ":" + newTimeArr[1] + " " + newTimeArr[2];
        }
        catch (ArrayIndexOutOfBoundsException a){
            Log.d("Time array OOB(((((((", "Exception: " + a);
        }
        timetext.setText("Time: " + newTime);

        TextView datetext = (TextView)findViewById(R.id.date);
        datetext.setText("Date: " + date);

        TextView comptext = (TextView)findViewById(R.id.compLevel);
        comptext.setText("Competitive Level: " + comp_level);

        TextView postal = (TextView)findViewById(R.id.postalAddress);
        String[] newAddrArr = redo(postalAddress);
        String newAddr = "";
        for(int i = 0; i < newAddrArr.length; i++){
            newAddr += newAddrArr[i] + " ";
        }
        postal.setText("Address: " + newAddr);

        TextView ownertext = (TextView)findViewById(R.id.owner);
        String[] newOwnArr = redo(user_id);
        String newOwn = newOwnArr[0];
        try{//I didn't find out until later that I would need to replace the dot too in the email address.
            newOwn += "@" + newOwnArr[1] + "." + newOwnArr[2];
        }
        catch (ArrayIndexOutOfBoundsException a){
            Log.d("Owner array OOB(((((((", "Exception: " + a);
        }
        ownertext.setText("Game Owner: " + newOwn);

        //Button to sign up
        final Button signup_button = (Button) findViewById(R.id.signup);
        if(view == 1 || view == 2){
            signup_button.setVisibility(View.GONE);
        }
        signup_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                su.gid = game_id;
                su.uid = uid;
                su.role = "player";
                su.starter();
            }
        });
        final Button ref_butt = (Button) findViewById(R.id.ref_button);
        if(view == 1 || view == 2){
            ref_butt.setVisibility(View.GONE);
        }
        ref_butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                su.role = "referee";
                su.uid = uid;
                su.starter();
            }
        });
        TextView orText = (TextView)findViewById(R.id.or);
        if(view == 1 || view == 2){
            orText.setVisibility(View.GONE);
        }
    }
    public String[] redo(String old){//A method for the part that is the same for each one that needs fixing
        StringTokenizer tokensNew = new StringTokenizer(old, "_");
        String[] newStr = new String[tokensNew.countTokens()];
        //Log.d("Count((((((((", "num: " + tokensNew.countTokens());
        for(int i = 0; i < newStr.length; i++){
            newStr[i] = tokensNew.nextToken();
            //Log.d("New String(((((((", "newStr " + i + ": " + newStr[i]);
        }
        return newStr;
    }
}