package com.example.ryan.places;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Ryan on 11/26/2017.
 */

public class LoginCheck {
    public String rst = "";//will contain the json string

    public String uid = "";

    public static Context context;

    public static boolean go_to_games = false;//prevent onPostExecute from going right to MyGames every login

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        fetch(uid);
    }

    public void fetch(String uid) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.uid = uid;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud
        /*final String ip_address = "192.168.1.19";//home
        final String project = "android_connect";
        final String file = "search.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/login_check/";

        String uid = "";

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            Login l = new Login();

            if(go_to_games) {
                go_to_games = false;
                l.acceptRes(result);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;

            builtUri += "?user_id=" + uid;

            String response = "";
            Log.d("HELLO*************", "doInBackground: " + builtUri);
            try {
                URL url = new URL(builtUri);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8192);
                    String jsonResp = null;
                    while ((jsonResp = input.readLine()) != null) {
                        response = response.concat(jsonResp);
                        //Log.d("HELLO*************", "While Loop");
                        Log.d("HELLO*************", "Response: " + response);
                        //Log.d("HELLO*************", "jsonResp: " + jsonResp);
                    }
                    input.close();
                }
            } catch (IOException e) {
                Log.d("HELLO*************", "HERES YOUR DANG STRING", e);
            }

            ////////////
            //Log.d("JSON Line", response);
            rst = response;//store the json string
            ///////////
            return response;
        }

        //return response;
    }
    //////////////////////////////////
}
