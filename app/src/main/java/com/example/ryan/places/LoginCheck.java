package com.example.ryan.places;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;
import static com.example.ryan.places.Login.idToken;

/**
 * Created by Ryan on 11/26/2017.
 */

/*
When the user logs in, the program needs to know whether they have ever used this app, and if they haven't,
they need to be added into the system. If they are in the system it returns a list of what they are signed up for.
 */

public class LoginCheck {
    public String rst = "";//will contain the json string

    //public String uid = "";

    public static Context context;

    public static boolean go_to_games = false;//prevent onPostExecute from going right to MyGames every login
    public static boolean signing_in = true;//for when the user is signing in. if false, delete the session

    public String idTok = "";

    CookieStore cookieStore = new BasicCookieStore();
    HttpContext localContext = new BasicHttpContext();

    public void starter(){
        //Log.d("HELLO*************", "Starter");
        fetch(idTok, signing_in);
    }

    public void fetch(String idt, boolean si) {
        //Log.d("HELLO*************", "Fetch");
        FetchMarkersTask fmt = new FetchMarkersTask();
        //move the user's search terms into the AsyncTask
        fmt.idTok = idt;
        fmt.signing_in = si;
        fmt.execute();
    }

    public class FetchMarkersTask extends AsyncTask<Void, Void, String> {

        //////////////////////////////////
        //these ip addresses are for testing with wampserver, will change to a better solution with google cloud
        /*final String ip_address = "192.168.1.18";//home
        final String project = "android_connect";
        final String file = "login_check.php";
        final String file_sign_out = "delete.php";*/

        boolean signing_in;

        String builtUri = "";

        //And, finally, the better solution with google cloud.
        final String onlineURL = "https://csc-182021.appspot.com/login_check/";
        final String onlineURL_sign_out = "https://csc-182021.appspot.com/delete/";

        String idTok = "";

        //@Override
        protected void onPostExecute(String result){
            Login l = new Login();

            if(go_to_games) {
                go_to_games = false;
                l.acceptRes(result, localContext);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d("TEST_TEST_TEST", title);
            HttpURLConnection urlConnection = null;

            /*
            Here is where it'll now either go to login_check.php, to sign the user in, or to delete.php, which destroys the session.
             */
            if(signing_in) {
                //for local:
                //builtUri = "http://" + ip_address + "/" + project + "/" + file;

                //for gcp
                builtUri = onlineURL;
            }
            else{
                //for local:
                //builtUri = "http://" + ip_address + "/" + project + "/" + file_sign_out;

                //for gcp
                builtUri = onlineURL_sign_out;
            }

            //builtUri += "?user_id=" + uid;

            String response1 = "";
            Log.d("HELLO*************", "doInBackground: " + builtUri);

            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            Log.d(TAG, "The LocalContext lc: " + localContext);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(builtUri);

            try {
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.d(TAG, "The ID Token: " + idToken);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                response1 = responseBody;
                Log.i(TAG, "Response from server (LC): " + responseBody);

            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            }

            ////////////
            //Log.d("JSON Line", response);
            rst = response1;//store the json string

            ///////////
            return response1;
        }

        //return response;
    }
    //////////////////////////////////
}
