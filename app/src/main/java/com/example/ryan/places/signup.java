package com.example.ryan.places;

import android.content.SharedPreferences;
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
import static com.example.ryan.places.GameInfo.title;
import static com.example.ryan.places.R.id.fun;
import static com.example.ryan.places.create.comp;

/**
 * Created by Ryan on 11/26/2017.
 */

/*
This is the background task that signs a user up for games
 */

public class signup {
    public String rst = "";//will contain the json string

    public static HttpContext localContext;

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
        /*final String ip_address = "192.168.1.18";//home
        final String project = "android_connect";
        final String file = "sign_up.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/sign_up/";

        String gid = "";
        String uid = "";
        String role = "";

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
                    gid + "&role=" + role;

            String response1 = "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(builtUri);

            try {
                Log.d(TAG, "The LocalContext signup: " + localContext);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                response1 = responseBody;
                Log.i(TAG, "Response from server (signup): " + responseBody);
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
