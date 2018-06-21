package com.example.ryan.places;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Ryan on 12/7/2017.
 */

/*
This is a background task that removes the user from a game if they choose to leave it
 */

public class Leave {
    public String rst = "";//will contain the json string

    public String gid = "";
    public String uid = "";

    public static HttpContext localContext;

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        Log.d("HELLO***********", "doInBackground uid: " + uid);
        fetch(gid, uid);
    }

    public void fetch(String gid, String uid) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.gid = gid;
        fmt.uid = uid;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {
        /*final String ip_address = "192.168.1.18";//home
        final String project = "android_connect";
        final String file = "leavegame.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/leavegame/";

        String gid = "";
        String uid = "";

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            GameInfo ga = new GameInfo();
            ga.toLogin();
        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;

            builtUri += "?gid=" +
                    gid;

            String response1 = "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(builtUri);

            try {
                Log.d(TAG, "The LocalContext leave: " + localContext);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                response1 = responseBody;
                Log.i(TAG, "Response from server (leave): " + responseBody);
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            }

            rst = response1;//store the json string
            ///////////
            return response1;
        }


        //return response;
    }
    //////////////////////////////////
}
