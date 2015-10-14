package com.example.user.mymapfinal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PassengerBOOKING extends FragmentActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDJqgBG9nIWSYbW9y7AZW8w40UM7_dnAt0";
    AutoCompleteTextView autoCompView;
    AutoCompleteTextView autoCompViewDES;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    JSONParser jsonParser = new JSONParser();
    private static final String BOOKING_URL = "http://taxires.site90.com/taxi/booking.php";
    String id;
    String address;
    private ProgressDialog pDialog;
    Spinner TypeSpinner;
    String lat,lng;
    EditText mEdit;
    ImageButton img;
    private GoogleMap mMap;
    Spinner spinnerV;
    private TimePicker timePicker1;
    private String format = "";
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_booking);
        spinnerV = (Spinner) findViewById(R.id.spinner2);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.pick_places);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        SharedPreferences s=getSharedPreferences("BookLocation", Context.MODE_PRIVATE);
        NavigationSource();

        autoCompView.setText(s.getString("sourceAdd", ""));

        autoCompViewDES = (AutoCompleteTextView) findViewById(R.id.des_places);

        autoCompViewDES.setAdapter(new GooglePlacesAutocompleteAdapterDestination(this, R.layout.list_item));
        autoCompViewDES.setOnItemClickListener(this);

        NavigationDes();
        autoCompViewDES.setText(s.getString("destAdd",""));


        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_array, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerV.setAdapter(adapter);
        spinnerV.setOnItemSelectedListener(this);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker);
        time =(TextView) findViewById(R.id.textView1);
        timePicker1.setIs24HourView(true);

        Button saveMe=(Button) findViewById(R.id.saveTime);

        saveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);

        img=(ImageButton) findViewById(R.id.imageButtoncal);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(v);

            }
        });

        Button book=(Button) findViewById(R.id.button1);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PassengerBooking().execute();

            }
        });

    }

    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 24;
            format = "AM";
        }
        else if (hour == 24) {
            format = "AM";
        } else if (hour > 24) {
            hour -= 24;
            format = "AM";
        } else {
            format = "PM";
        }
        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" "));
    }


    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }
    public void populateSetDate(int year, int month, int day) {
        mEdit = (EditText)findViewById(R.id.bookDate);
        mEdit.setText(month+"/"+day+"/"+year);
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
    }

    public void getLatLongFromAddress(String youraddress) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(youraddress) + "&sensor=false";
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            Double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            Double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", String.valueOf(lat));
            Log.d("longitude", String.valueOf(lng));
            String autosource=String.valueOf(lat)+","+ String.valueOf(lng);
            Log.e("autosource", autosource);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("BookLocation", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("source", autosource);

            editor.commit();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getLatLongFromAddressDes(String youraddress) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(youraddress) + "&sensor=false";
        //URLEncoder.encode(uri)
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            Double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            Double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", String.valueOf(lat));
            Log.d("longitude", String.valueOf(lng));
            String autodes=String.valueOf(lat)+","+ String.valueOf(lng);
            Log.e("autodes", autodes);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("BookLocation", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("dest", autodes);

            editor.commit();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private boolean validate(){
        if(autoCompView.getText().toString().trim().equals(""))
            return false;
        else if(autoCompViewDES.getText().toString().trim().equals(""))
            return false;
            // else if(etTwitter.getText().toString().trim().equals(""))
            //   return false;
        else
            return true;
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:lk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();


                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList previewDetails(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:lk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapterDestination extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapterDestination(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }


        @Override
        public Filter getFilter() {


            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = previewDetails(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                        //getLatLongFromAddressDes(constraint.toString());
                    }
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();

                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public void NavigationSource(){
        //ImageButton a = (ImageButton) findViewById(R.id.btn_help);
        ImageButton ab = (ImageButton)findViewById(R.id.imageButton);
        //Button a=(Button)findViewById(R.id.btn_help);
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BookSourceMap.class));
            }
        });
    }

    public void NavigationDes(){
        //ImageButton a = (ImageButton) findViewById(R.id.btn_help);
        ImageButton ab = (ImageButton)findViewById(R.id.imageButton2);
        //Button a=(Button)findViewById(R.id.btn_help);
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BookDesMap.class));
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_passenger_booking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class PassengerBooking extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PassengerBOOKING.this);
            pDialog.setMessage("Requesting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            // pDialog.dismiss();
        }


        private boolean isValidPassword(String pass) {
            if (pass != null && pass.length() >= 3) {
                return true;
            }
            return false;
        }
        @Override
        protected String doInBackground(String... args) {

       /*
        TODO Auto-generated method stub
        Check for success tag
        */

            int success;

            try {

                String Booking_date=mEdit.getText().toString();

                String Booking_time=time.getText().toString();


                String autoSourceAdd=autoCompView.getText().toString();
                String autoDestAdd=autoCompViewDES.getText().toString();

                getLatLongFromAddress(autoSourceAdd);
                getLatLongFromAddressDes(autoDestAdd);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("BookLocation", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("sourceAdd",autoSourceAdd);
                editor.putString("destAdd",autoDestAdd);

                editor.commit();


                // SharedPreferences s=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences s1=getSharedPreferences("BookLocation", Context.MODE_PRIVATE);
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("vehicle_type",spinnerV.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("PickupGeo", s1.getString("source", "") ));
                params.add(new BasicNameValuePair("DesGeo", s1.getString("dest", "")));
                params.add(new BasicNameValuePair("Pickup_Location", s1.getString("sourceAdd", "") ));
                params.add(new BasicNameValuePair("Destination",  s1.getString("destAdd", "")));
                params.add(new BasicNameValuePair("Booking_date",  Booking_date));
                params.add(new BasicNameValuePair("Booking_time",  Booking_time));


                JSONObject json = jsonParser.makeHttpRequest( BOOKING_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                    Log.d("Requested!", json.toString());

                    return json.getString(TAG_MESSAGE);


                }else{

                    Log.d("Request Failure!", json.getString(TAG_MESSAGE));

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
                // Toast.makeText(Reservation.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

}
