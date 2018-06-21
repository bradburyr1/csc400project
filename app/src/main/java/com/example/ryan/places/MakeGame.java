package com.example.ryan.places;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import static com.example.ryan.places.R.id.fun;

/**
 * Created by Ryan on 10/1/2017.
 */

/*
This is the background task that adds games to the database
 */

public class MakeGame {
    public static String rst = "";//will contain the json string

    public static HttpContext localContext;

    String sport = "";
    String city = "";
    String time = "";
    String address = "";
    String date = "";
    String comp = "";
    String lat = "";
    String lng = "";
    String uid = "";
    String players = "";
    String refs = "";


    public void starter(){
        Log.d("HELLO*************", "Starter");
        fetch(sport, city, time, address, date, comp, lat, lng, uid, players, refs);
    }

    public void fetch(String sport, String city, String time, String address, String date,
                      String comp, String lat, String lng, String uid, String players, String refs) {
        Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.sport = sport;
        fmt.city = city;
        fmt.time = time;
        fmt.address = address;
        fmt.date = date;
        fmt.comp = comp;
        fmt.lat = lat;
        fmt.lng = lng;
        fmt.uid = uid;
        fmt.players = players;
        fmt.refs = refs;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {
        /*final String ip_address = "192.168.1.18";//home
        final String project = "android_connect";
        final String file = "create_game.php";*/

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/create_game/";

        String sport = "";
        String city = "";
        String time = "";
        String address = "";
        String date = "";
        String comp = "";
        String lat = "";
        String lng = "";
        String uid = "";
        String players = "";
        String refs = "";

        //for local:
        //String builtUri = "http://" + ip_address + "/" + project + "/" + file;

        //for gcp
        String builtUri = onlineURL;

        //@Override
        protected void onPostExecute(String result){
            /*if(rst.equals("Insertion successful")){
                Toast.makeText(this, "Game created", Toast.LENGTH_LONG).show();
            }*/
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("TEST_TEST_TEST", sport);
            HttpURLConnection urlConnection = null;

            sport = sport.toLowerCase();
            city = city.toLowerCase();
            time = time.toLowerCase();
            address = address.toLowerCase();
            date = date.toLowerCase();

            builtUri += "?sport=" +
                    sport + "&city=" + city + "&time=" + time + "&address=" + address +
                    "&date=" + date + "&comp=" + comp + "&lat=" + lat +
                    "&long=" + lng + "&players=" + players + "&refs=" + refs;

            String response1 = "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(builtUri);

            try {
                Log.d(TAG, "The LocalContext makegame: " + localContext);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                response1 = responseBody;
                Log.i(TAG, "Response from server (makegame): " + responseBody);
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
