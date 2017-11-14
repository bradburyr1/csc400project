package com.example.ryan.places;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameInfo extends AppCompatActivity {

    public static String title;
    public static String game_id;
    public static String city;
    public static String time;
    public static String date;
    public static String comp_level;
    public static String postalAddress;
    public static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
    }
}
