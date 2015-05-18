
package test.ShareLocation.WeLocate;

import test.ShareLocation.WeLocate.helper.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {
    // LogCat tag
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnSearchFriend;
    private Button btnMyLocation;
    private Button btnLogOut;
    private Button btnInviteFriends;
    private Button btnResetPIN;


    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnSearchFriend = (Button) findViewById(R.id.btnSearchFriend);
        btnMyLocation = (Button) findViewById(R.id.btnMyLocation);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnInviteFriends= (Button) findViewById(R.id.btnInvite);
        btnResetPIN= (Button) findViewById(R.id.resetPin);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
 /*       if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        btnLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnSearchFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchFriend();
            }
        });
        btnMyLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myLocation();
            }
        });
        btnInviteFriends.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                inviteFriends();
            }
        });
        btnResetPIN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetPin();
            }
        });



    }

    private void logoutUser() {
        session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void inviteFriends() {
        session.setLogin(false);

        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MenuActivity.this, Request.class);
        startActivity(intent);
        finish();
    }

    private void resetPin() {
        session.setLogin(false);
        String name= getIntent().getStringExtra("name");
        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MenuActivity.this, ResetPinActivity.class);
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("name", name);


        //Add the bundle to the intent
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void searchFriend() {
        // Launching the search activity
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void myLocation() {

        String name= getIntent().getStringExtra("name");
        String lon= getIntent().getStringExtra("lon");
        String lat= getIntent().getStringExtra("lat");
        String email= getIntent().getStringExtra("email");


       // Launching the map activity
        Intent intent = new Intent(MenuActivity.this, MyMapsActivity.class);
        Bundle bundle = new Bundle();

        //Add your data to bundle
        //bundle.putString("name", uid);
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("lon", lon);
        bundle.putString("lat", lat);

        //Add the bundle to the intent
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
