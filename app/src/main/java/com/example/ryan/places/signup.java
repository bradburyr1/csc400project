package com.example.ryan.places;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.ryan.places.GameInfo.title;
import static com.example.ryan.places.R.id.fun;
import static com.example.ryan.places.create.comp;

/**
 * Created by Ryan on 11/26/2017.
 */

public class signup {
    public String rst = "";//will contain the json string

    public String gid = "";
    public String uid = "";
    public String role = "";

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        fetch(gid, uid, role);
    }

    public void fetch(String gid, String uid, String role) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.gid = gid;
        fmt.uid = uid;
        fmt.role = role;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud
        /*final String ip_address = "192.168.1.19";//home
        final String project = "android_connect";
        final String file = "search.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/";

        String gid = "";
        String uid = "";
        String role = "";

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            //MapsActivity ma = new MapsActivity();
            SearchActivity sa = new SearchActivity();
            //Log.d("ONMAPREADY222222--", "ONMAPREADY");
            //ma.parseJSON(result);
            sa.acceptRes(result);
        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;

            builtUri += "?gid=" +
                    gid + "&uname=" + uid + "&role=" + role;

            String response = "";
            Log.d("HELLO*************", "doInBackground: " + builtUri);
            /*try {
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
            }*///////////

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
