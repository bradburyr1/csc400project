package com.example.ryan.places;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameInfo extends AppCompatActivity {

    public static String title = "";
    public static String game_id = "";
    public static String city = "";
    public static String time = "";
    public static String date = "";
    public static String comp_level = "";
    public static String postalAddress = "";
    public static String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);


        //This is where all the textviews get their information set to them
        TextView sport = (TextView)findViewById(R.id.sport);
        sport.setText(title);

        TextView citytext = (TextView)findViewById(R.id.city);
        citytext.setText(city);

        TextView timetext = (TextView)findViewById(R.id.time);
        timetext.setText(time);

        TextView datetext = (TextView)findViewById(R.id.date);
        datetext.setText(date);

        TextView comptext = (TextView)findViewById(R.id.compLevel);
        comptext.setText(comp_level);

        TextView postal = (TextView)findViewById(R.id.postalAddress);
        postal.setText(postalAddress);

        TextView ownertext = (TextView)findViewById(R.id.owner);
        ownertext.setText(user_id);
    }
}
