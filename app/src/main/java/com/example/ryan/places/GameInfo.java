package com.example.ryan.places;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.StringTokenizer;

import static android.R.attr.button;
import static android.os.Build.VERSION_CODES.M;

/*
This activity shows the information about a game, such as what sport, when, where, etc.
 */

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
    public static String max_players = "";
    public static String max_refs = "";
    public static String curr_players = "";
    public static String curr_refs = "";

    public static Context context;

    static int view = 0;//0: from map
                        //1: games signed up for
                        //2: games owned

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        context = getApplicationContext();

        //This is where all the textviews get their information set to them
        //Sport
        TextView sport = (TextView)findViewById(R.id.sport);
        String[] newSportArr = redo(title);
        String newTitle = "";
        for(int i = 0; i < newSportArr.length; i++){
            newTitle += newSportArr[i] + " ";
        }
        sport.setText("Sport: " + newTitle);

        //City
        TextView citytext = (TextView)findViewById(R.id.city);
        String[] newCityArr = redo(city);
        String newCity = "";
        for(int i = 0; i < newCityArr.length; i++){
            newCity += newCityArr[i] + " ";
        }
        citytext.setText("City: " + newCity);

        //Time
        TextView timetext = (TextView)findViewById(R.id.time);
        Log.d("Time Old Text(((((((", "Old Text: " + time);
        String[] newTimeArr = redo(time);
        String newTime = newTimeArr[0];
        try{//Some of the test games don't have the time formatted correctly (early example games)
            newTime += ":" + newTimeArr[1];
        }
        catch (ArrayIndexOutOfBoundsException a){
            Log.d("Time array OOB(((((((", "Exception: " + a);
        }
        try{//Some of the test games don't have the time formatted correctly (early example games)
            newTime += " " + newTimeArr[2];
        }
        catch (ArrayIndexOutOfBoundsException a){
            Log.d("Time array OOB(((((((", "Exception: " + a);
        }
        timetext.setText("Time: " + newTime);

        //Date
        TextView datetext = (TextView)findViewById(R.id.date);
        datetext.setText("Date: " + date);

        //Competition level
        TextView comptext = (TextView)findViewById(R.id.compLevel);
        comptext.setText("Competitive Level: " + comp_level);

        //Address
        TextView postal = (TextView)findViewById(R.id.postalAddress);
        String[] newAddrArr = redo(postalAddress);
        String newAddr = "";
        for(int i = 0; i < newAddrArr.length; i++){
            newAddr += newAddrArr[i] + " ";
        }
        postal.setText("Address: " + newAddr);

        //Owner
        TextView ownertext = (TextView)findViewById(R.id.owner);
        String[] newOwnArr = redo(user_id);
        String newOwn = newOwnArr[0];
        try{//Some of the earlier ones aren't formatted correctly
            newOwn += "@" + newOwnArr[1];
        }
        catch (ArrayIndexOutOfBoundsException a){
            Log.d("Owner array OOB(((((((", "Exception: " + a);
        }
        try{//I didn't find out until later that I would need to replace the dot too in the email address.
            newOwn += "." + newOwnArr[2];
        }
        catch (ArrayIndexOutOfBoundsException a){
            Log.d("Owner array OOB(((((((", "Exception: " + a);
        }
        ownertext.setText("Game Owner: " + newOwn);

        int currRefInt = Integer.parseInt(curr_refs);
        int currPlayInt = Integer.parseInt(curr_players);
        int maxRefInt = Integer.parseInt(max_refs);
        int maxPlayInt = Integer.parseInt(max_players);

        //Button to sign up
        final Button signup_button = (Button) findViewById(R.id.signup);
        final Button ref_butt = (Button) findViewById(R.id.ref_button);

        if(view == 1 || view == 2|| currPlayInt >= maxPlayInt){
            signup_button.setVisibility(View.GONE);
        }
        signup_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Signed up as a player";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                signup_button.setVisibility(View.GONE);
                ref_butt.setVisibility(View.GONE);

                su.gid = game_id;
                su.uid = uid;
                su.role = "player";
                su.starter();
            }
        });

        //Button to become a referee
        if(view == 1 || view == 2 || currRefInt >= maxRefInt){
            ref_butt.setVisibility(View.GONE);
        }
        ref_butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Signed up as a referee";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                signup_button.setVisibility(View.GONE);
                ref_butt.setVisibility(View.GONE);

                su.role = "referee";
                su.gid = game_id;
                su.uid = uid;
                su.starter();
            }
        });

        //Leave game
        final Button leave_butt = (Button) findViewById(R.id.leave_butt);
        if(view == 1){
            leave_butt.setVisibility(View.VISIBLE);
        }
        else {
            leave_butt.setVisibility(View.GONE);
        }
        leave_butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Leave le = new Leave();
                //Show a message
                Context context = getApplicationContext();
                CharSequence text = "You left the game";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                le.gid = game_id;
                le.uid = uid;
                le.starter();
            }
        });

        //See who's attending a game
        final Button attend = (Button) findViewById(R.id.attend_butt);
        if(view == 1 || view == 2){
            Log.d("HELLOvvvvv", "should be visible: " + view);
            attend.setVisibility(View.VISIBLE);
        }
        else {
            attend.setVisibility(View.GONE);
        }
        attend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GetAttendees ga = new GetAttendees();
                ga.gid = game_id;
                ga.starter();
            }
        });

        //Set the text for signed_up
        TextView signedText = (TextView)findViewById(R.id.signed_view);
        String signed_content = "";
            signed_content = "Players: " + curr_players + "/" + max_players;
        signedText.setText(signed_content);

        //Set the text for refs_view
        TextView refText = (TextView)findViewById(R.id.refs_view);
        String ref_content = "";
            ref_content = "Referees: " + curr_refs + "/" + max_refs;
        refText.setText(ref_content);
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
    public void acceptRes(String result){
        Attendees at = new Attendees();
        at.result = result;

        try{
            at.parseJSON();
        }
        catch(IOException e)
        {
            Log.d("IO******EXCEPTION", "(acceptRes) Exception: " + e);
        }
        Intent i = new Intent(context, Attendees.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    //Go to login. This will prevent people from trying to leave games more than once
    public void toLogin(){
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}