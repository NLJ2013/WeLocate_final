package test.ShareLocation.WeLocate;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import test.ShareLocation.WeLocate.app.AppConfig;
import test.ShareLocation.WeLocate.app.AppController;
import test.ShareLocation.WeLocate.helper.SQLiteHandler;
import test.ShareLocation.WeLocate.helper.SessionManager;

public class MyMapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        String lon= getIntent().getStringExtra("lon");
        String lat= getIntent().getStringExtra("lat");
        String email= getIntent().getStringExtra("email");
        String name= getIntent().getStringExtra("name");

        mMap.setMyLocationEnabled(true);
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Get latitude of the current location
        double latitude = myLocation.getLatitude();
        // Get longitude of the current location
        double longitude = myLocation.getLongitude();
        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Current Location").snippet("Latitude " + latitude + ", Longitude " + longitude));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title("Your Friend's Location").snippet("Latitude " + lat + ", Longitude " + lon));

        updateUser(email, String.valueOf(latitude), String.valueOf(longitude));
    }

    private void updateUser(final String email, final String lat, final String lon) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

   /*     pDialog.setMessage("Updating ...");
        showDialog();*/

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Updating Response: " + response.toString());
                //hideDialog();

		     }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Registration Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
				hideDialog();
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
        AppController.getInstance().addToRequestQueue(strReq);



        Toast.makeText(getApplicationContext(),
                "Your location Is Updated", Toast.LENGTH_LONG)
                .show();
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
