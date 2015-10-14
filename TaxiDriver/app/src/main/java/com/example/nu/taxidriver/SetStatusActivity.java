package com.example.nu.taxidriver;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapController;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SetStatusActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = SetStatusActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private Switch mySwitch;
   public String switchStatus;
    boolean avail=false;

    private ProgressDialog pDialog;
    public String email;
    public double Latitude;
    public double Longitude;
    JSONParser jsonParser = new JSONParser();
    private static final String URL = "http://taxires.site90.com/taxi/setAvailability.php";
    private static final String URL2 = "http://taxires.site90.com/taxi/getAvailability.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_status);
        SharedPreferences pref = getSharedPreferences("MyPref", 0);


        email= pref.getString("email", "");
        new GetStatus().execute();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser();
        }else{

        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mySwitch = (Switch) findViewById(R.id.Switch);
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


               if(isChecked){
                    switchStatus="AVAILABLE";
                   callAsynchronousTask();
                }else{
                    switchStatus="UNAVAILABLE";
                    new SetAvailability().execute();
                }


            }
        });


    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    class SetAvailability extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SetStatusActivity.this);
            pDialog.setMessage("Saving your location...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            // pDialog.dismiss();
        }



        @Override
        protected String doInBackground(String... args) {

           /*
            TODO Auto-generated method stub
            Check for success tag
            */

            int success;


            try {



                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("status",switchStatus));

                params.add(new BasicNameValuePair("email",email));
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Latitude=location.getLatitude();
                Longitude=location.getLongitude();
                params.add(new BasicNameValuePair("latitude",String.valueOf(Latitude)));
                params.add(new BasicNameValuePair("longitude",String.valueOf(Longitude)));


                Log.d("request!", "starting"+Longitude+Latitude);



                JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);



                Log.d("Status Change attempt", json.toString());



                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Log.d("Status Changed!", json.toString());



                    return json.getString(TAG_MESSAGE);

                }else{

                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String file_url){

            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(SetStatusActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

    class GetStatus extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SetStatusActivity.this);
            pDialog.setMessage("Retrieving your status...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
//            pDialog.show();

            // pDialog.dismiss();
        }



        @Override
        protected String doInBackground(String... args) {

           /*
            TODO Auto-generated method stub
            Check for success tag
            */

            int success;


            try {



                List<NameValuePair> params = new ArrayList<NameValuePair>();



                params.add(new BasicNameValuePair("email",email));



                Log.d("request!", "starting"+email);



                JSONObject json = jsonParser.makeHttpRequest(URL2, "POST", params);



                Log.d("Status Change attempt", json.toString());



                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    String s=json.getString("status");
                      if(s.equals("AVAILABLE"))
                        avail=true;
                    Log.d("Status Changed!", json.toString());


                    return json.getString(TAG_MESSAGE);

                }else{

                    Log.d("Cannot retrieve status!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;

        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        protected void onPostExecute(String file_url){

          //  pDialog.dismiss();
            if (file_url != null){
                if(avail)
                    mySwitch.setChecked(true);

                else
                    mySwitch.setChecked(false);
                Toast.makeText(SetStatusActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(values[0]=="AVAILABLE")
            mySwitch.setChecked(true);
            else
            mySwitch.setChecked(false);
        }

    }
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new SetAvailability().execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 300000); //execute in every 5 mins
    }
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
   private void setUpMap() {
       mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }
        else {
            handleNewLocation(location);
        };

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
       double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mycars))

                .flat(true)
                .rotation(245)
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        float zoomlevel=14;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomlevel));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(13)
                .bearing(90)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);

    }


}
