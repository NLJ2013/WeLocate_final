package test.ShareLocation.WeLocate;

import test.ShareLocation.WeLocate.app.AppConfig;
import test.ShareLocation.WeLocate.app.AppController;
import test.ShareLocation.WeLocate.helper.SQLiteHandler;
import test.ShareLocation.WeLocate.helper.SessionManager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				// Check for empty data in the form
				if (email.trim().length() > 0 && password.trim().length() > 0) {
					// login user
					checkLogin(email, password);

				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please enter the credentials!", Toast.LENGTH_LONG)
							.show();
				}
			}

		});


		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	private void updateUser(final String email, final String lat, final String lon) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, "Updating Response: " + response.toString());
				hideDialog();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				/*Log.e(TAG, "Registration Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
				hideDialog();*/
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "update");
				params.put("name", email);
				params.put("email", lat);
				params.put("password", lon);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
		Toast.makeText(getApplicationContext(),
				"Your Location Is Updated", Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String email, final String password) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		showDialog();
		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_LOGIN, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Login Response: " + response.toString());
						hideDialog();
						try {
							JSONObject jObj = new JSONObject(response);
							boolean error = jObj.getBoolean("error");
							// Check for error node in json
							if (!error) {
								// user successfully logged in
                                String uid = jObj.getString("name");
                                String lat = jObj.getString("lat");
                                String lon = jObj.getString("lon");

								Toast.makeText(getApplicationContext(),	"Welcome back "+uid, Toast.LENGTH_LONG).show();
								// Create login session
								session.setLogin(true);

								// Launch main activity
								Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

                                //Create the bundle
                                Bundle bundle = new Bundle();

                                //Add your data to bundle
                                bundle.putString("name", uid);
                                bundle.putString("email", email);
                                bundle.putString("lon", lon);
                                bundle.putString("lat", lat);

                                //Add the bundle to the intent
                                intent.putExtras(bundle);

								startActivity(intent);
								finish();
							} else {
                                session.setLogin(false);
								// Error in login. Get the error message
								String errorMsg = jObj.getString("error_msg");
								Toast.makeText(getApplicationContext(),	errorMsg, Toast.LENGTH_LONG).show();
							}

						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Login Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "login");
				params.put("email", email);
				params.put("password", password);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
