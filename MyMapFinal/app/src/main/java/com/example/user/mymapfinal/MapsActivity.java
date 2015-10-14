package com.example.user.mymapfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity  {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    MarkerOptions markerOptions;
    LatLng latLng;
    double lat,lng;
    String addressText;
    String id;
    String source;
    String location;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    SharedPreferences sharedPreferences;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    int locationCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SRILANKA.getCenter(), 8));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("Location", 0);
        settings.edit().remove("dest").commit();
        settings.edit().remove("destAdd").commit();
            SupportMapFragment supportMapFragment = (SupportMapFragment)
                    getSupportFragmentManager().findFragmentById(R.id.map);
            Button confirm = (Button) findViewById(R.id.btnconfirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), Reservation.class);

                    source= String.valueOf(lat)+","+String.valueOf(lng);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Location", 0);
                    SharedPreferences.Editor editor = pref.edit();


                    editor.putString("dest", source);
                    editor.putString("destAdd",id);



                    editor.commit();
                    finish();
                    startActivity(i);

                }
            });

            // Getting a reference to the map

            mMap.setMyLocationEnabled(true);

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {

                // Getting the Latitude and Longitude of the touched location
                latLng = arg0;

                // Clears the previously touched position
                //mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Creating a marker
                markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                // Adding Marker on the touched location with address
                new GetAddressTask().execute(latLng);

                // new ReverseGeocodingTask(MainActivity.this).execute(latLng);

            }
        });


    }

    private LatLngBounds SRILANKA = new LatLngBounds(
            new LatLng(5.850,79.600), new LatLng(9.920, 81.950));



    public class GetAddressTask extends AsyncTask<LatLng, String, Integer> {
        private LatLng loc;
        String filterAddress = "";
        String addressText="";



        @Override
        protected Integer doInBackground(LatLng... params) {
            int mFinalFlag = 0;
            loc = params[0];


            Geocoder geoCoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(loc.latitude,
                        loc.longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    addressText = String.format(
                            "%s, %s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getLocality(),
                            address.getCountryName());
                    lat = addresses.get(0).getLatitude();
                    lng = addresses.get(0).getLongitude();
                    Log.e("lat", "" + lat);
                    Log.e("lng", "" + lng);


                }


            } catch (IOException ex) {
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.publishProgress(addressText);

            return mFinalFlag;
        }


        @Override
        protected void onPostExecute(Integer result) {

            mMap.addMarker(
                    new MarkerOptions()
                            .position(loc)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title(addressText))


                    .showInfoWindow();

            super.onPostExecute(result);



        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            id= values[0].toString();



        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
