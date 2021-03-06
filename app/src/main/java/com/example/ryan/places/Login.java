package com.example.ryan.places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ryan.places.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.StringTokenizer;

import static android.os.Build.VERSION_CODES.M;
import static com.example.ryan.places.create.time;

/*
This activity acts as the main menu to the app, where the user will sign in, sign out, and go to the other activities from.
 */

public class Login extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    create cr = new create();
    MapsActivity ma = new MapsActivity();
    LoginCheck lc = new LoginCheck();

    public static String uid = "";
    public static String idToken = "";

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();

        // Views
        mStatusTextView = (TextView)findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity sa = new SearchActivity();
                //Log.d("HELLOttttttttt", "Login: " + uid);
                sa.localContext = lc.localContext;
                sa.uid = uid;
                startActivity(new Intent(Login.this, SearchActivity.class));
            }
        });

        Button createbutton = (Button) findViewById(R.id.create_button);
        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create c = new create();
                c.localContext = lc.localContext;
                startActivity(new Intent(Login.this, create.class));
            }
        });
        Button signedbutton = (Button) findViewById(R.id.signed_up);//button that goes to MyGames with everything the user is signed up for
        signedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameInfo gi = new GameInfo();
                gi.view = 1;
                gi.localContext = lc.localContext;
                gi.uid = uid;
                lc.go_to_games = true;
                lc.idTok = idToken;
                lc.starter();
            }
        });
        Button ownedButton = (Button) findViewById(R.id.owned);//button that goes to MyGames with everything the user is signed up for
        ownedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameInfo gi = new GameInfo();
                gi.view = 2;
                GetOwned go = new GetOwned();
                go.idTok = idToken;
                go.localContext = lc.localContext;
                //go.uid = uid;
                go.starter();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getEmail()));
            idToken = acct.getIdToken();
            String email = acct.getEmail();
            StringTokenizer tokenTime = new StringTokenizer(email, "@.");
            String name = tokenTime.nextToken();
            String suffix = tokenTime.nextToken();
            String last = tokenTime.nextToken();
            String newTok = "_";
            email = name + newTok + suffix + newTok + last;
            uid = email;
            cr.uid = email;//We need change uid in the information on "makegame" in case the user decides to make a game
            ma.uid = email;

            //Login Check. This is to make sure that when the user logs in, they are in the system. If they're not, put them in.
            lc.idTok = idToken;
            lc.signing_in = true;
            lc.starter();

            Log.d("%%%%%%%%%%%%%%%%%%%%%%", acct.getDisplayName());
            updateUI(true);
            //startActivity(new Intent(Login.this, WelcomeScreen.class));
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
            Log.d("%%%%%%%%%%%%%%%%%%%%%%", ""+result.getStatus());
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            findViewById(R.id.create_button).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
            findViewById(R.id.owned).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_up).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            findViewById(R.id.create_button).setVisibility(View.GONE);
            findViewById(R.id.owned).setVisibility(View.GONE);
            findViewById(R.id.signed_up).setVisibility(View.GONE);
            cr.uid = "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                lc.signing_in = false;
                lc.starter();
                signOut();
                break;
        }
    }


    /////////////
    public void acceptRes(String result, HttpContext LC){
        MyGames mg = new MyGames();
        mg.result = result;
        mg.localContext = LC;
        Log.d("login AR", "The LocalContext GA: " + mg.localContext);

        try{
            mg.parseJSON();
        }
        catch(IOException e)
        {
            Log.d("IO******EXCEPTION", "(acceptRes) Exception: " + e);
        }
        Intent i = new Intent(context, MyGames.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Log.d("login AR", "The LocalContext GA: " + mg.localContext);

        context.startActivity(i);
    }
}