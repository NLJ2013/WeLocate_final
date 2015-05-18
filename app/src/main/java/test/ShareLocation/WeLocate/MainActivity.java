package test.ShareLocation.WeLocate;

import test.ShareLocation.WeLocate.app.AppConfig;
import test.ShareLocation.WeLocate.app.AppController;
import test.ShareLocation.WeLocate.helper.SQLiteHandler;
import test.ShareLocation.WeLocate.helper.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLocate;
	private Button btnLogout;

	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.pin);
		btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLocate =(Button) findViewById(R.id.btnLocate);

		// session manager
		session = new SessionManager(getApplicationContext());



		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});


        // Locate button Click Event

        btnLocate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String userName = inputEmail.getText().toString();
                String PIN = inputPassword.getText().toString();

                // Check for empty data in the form
                if (userName.trim().length() > 0 && PIN.trim().length() > 0) {
                    // login user
                    getFriendLocation(userName, PIN);

                } else {
                    // Prompt user to enter credentials
                    getFriendLocation(userName, PIN);
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

	}






    /**
     * function to get friends location details from db
     * */
    private void getFriendLocation(final String userName, final String PIN) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {
                        String uid = jObj.getString("name");
                        String lat = jObj.getString("lat");
                        String lon = jObj.getString("lon");

                        //updateUser(email,"123","456");

                        // user successfully logged in
                        Toast.makeText(getApplicationContext(),
                                "Your Friend "+uid+". Is at Lat: "+lat + ", Lon: "+lon , Toast.LENGTH_LONG).show();
                        // Create login session
                        session.setLogin(true);
                        // Launch main activity

                        Intent intent = new Intent(MainActivity.this,
                                MapsActivity.class);

                        //Create the bundle
                        Bundle bundle = new Bundle();

                        //Add your data to bundle
                        bundle.putString("name", uid);
                        bundle.putString("email", userName);
                        bundle.putString("lon", lon);
                        bundle.putString("lat", lat);
                        bundle.putString("PIN", PIN);

                        //Add the bundle to the intent
                        intent.putExtras(bundle);

                        startActivity(intent);
                        finish();
                    } else {
                        session.setLogin(false);
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url

                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "LocateFriend");
                params.put("email", userName);
                params.put("password", PIN);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


	private void logoutUser() {
		session.setLogin(false);

		//db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
