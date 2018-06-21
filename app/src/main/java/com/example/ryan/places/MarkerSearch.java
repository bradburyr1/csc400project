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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.content.ContentValues.TAG;

/**
 * Created by Ryan on 10/1/2017.
 */

/*
This is the background task for searching for games to play in. Games the user has already signed up for are not shown.
Results will automatically go to the map, but a button is on the map to show as a list. 
 */

public class MarkerSearch {
    public String rst = "";//will contain the json string

    public static HttpContext localContext;

    public String title = "any";
    public String city = "any";
    public boolean comp = false;
    public boolean fun = false;
    public String uid = "";

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        fetch(title, city, comp, fun, uid);
    }

    public void fetch(String title, String city, boolean comp, boolean fun, String uid) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.title = title;
        fmt.city = city;
        fmt.comp = comp;
        fmt.fun = fun;
        fmt.uid = uid;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {
        /*final String ip_address = "192.168.1.18";//home
        final String project = "android_connect";
        final String file = "search.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/";

        String title = "any";
        String city = "any";
        boolean comp = false;
        boolean fun = false;
        String uid = "";

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

            if(title.equals("")){
                title = "any";
            }
            if(city.equals("")) {
                city = "any";
            }
            title = title.toLowerCase();
            city = city.toLowerCase();

            builtUri += "?sport=" +
                    title + "&city=" + city + "&comp=" + comp + "&fun=" + fun;

            String response1 = "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(builtUri);

            try {
                Log.d(TAG, "The LocalContext markersearch: " + localContext);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                response1 = responseBody;
                Log.i(TAG, "Response from server (markersearch): " + responseBody);
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
