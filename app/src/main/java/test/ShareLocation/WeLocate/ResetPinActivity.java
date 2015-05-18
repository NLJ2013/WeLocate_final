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

public class ResetPinActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnresetPin;
	private EditText txtCurrPin;
	private EditText txtNewPin;
	private EditText txtConfirmPin;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pin);

        txtCurrPin = (EditText) findViewById(R.id.txtcurrPin);
        txtNewPin = (EditText) findViewById(R.id.txtNewPin);
        txtConfirmPin = (EditText) findViewById(R.id.txtConfirmPin);
        btnresetPin = (Button) findViewById(R.id.btnReset);



		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not


		// Login button Click Event
		btnresetPin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String currentPin = txtCurrPin.getText().toString();
				String newPin = txtNewPin.getText().toString();
				String confirmPin = txtConfirmPin.getText().toString();

				// Check for empty data in the form
				if (currentPin.trim().length() > 0 && newPin.trim().length() > 0&& confirmPin.trim().length() > 0) {
                    if(newPin.equals(confirmPin)) {                    // login user
                        resetPin(currentPin,newPin);
                    }else{
                            Toast.makeText(getApplicationContext(),
                                "Passwords Do Not Match!", Toast.LENGTH_LONG)
                                .show();

                    }
				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please enter the credentials!", Toast.LENGTH_LONG)
							.show();
				}
			}

		});



	}

	private void resetPin(final String currentPin, final String newPin) {
		// Tag used to cancel the request

		String tag_string_req = "req_login";

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {

                        session.setLogin(true);
                        // Launch main activity

                        Intent intent = new Intent(ResetPinActivity.this,
                                MenuActivity.class);


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




				Log.d(TAG, "Updating Response: " + response.toString());
				hideDialog();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Reset PIN Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
                String name= getIntent().getStringExtra("name");
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "reset");
				params.put("currentPin", currentPin);
				params.put("newPin", newPin);
				params.put("name", name);


				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
