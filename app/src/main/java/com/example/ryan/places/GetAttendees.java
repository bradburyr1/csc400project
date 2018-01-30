package com.example.ryan.places;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ryan on 12/7/2017.
 */

/*
This is the background task that gets the information from the server about who is attending a game, which will be
displayed in the attendee activity
 */

public class GetAttendees {
    public String rst = "";//will contain the json string

    public String gid = "";

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        fetch(gid);
    }

    public void fetch(String gid) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.gid = gid;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {
        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/attendees/";

        String gid = "";

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            GameInfo gi = new GameInfo();
            gi.acceptRes(result);
        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;

            builtUri += "?gid=" +
                    gid;

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
