
package com.parse.carpool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.integratingfacebooktutorial.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassengerMapActivity extends ActionBarActivity {

    AutoCompleteTextView atvPlaces;
    AutoCompleteTextView atvPlaces2;
    DownloadTask placesDownloadTask;
    DownloadTask placeDetailsDownloadTask;
    ParserTask placesParserTask;
    ParserTask placeDetailsParserTask;
    GoogleMap googleMap;

    boolean atvPlacesChecking = false;
    boolean atvPlacesChecking2 = false;
    final int PLACES=0;
    final int PLACES_DETAILS=1;
    final ArrayList<Marker> markerList = new ArrayList<Marker>();
    final ArrayList<Marker> markerList2 = new ArrayList<Marker>();
    final ArrayList<Double>  destination = new ArrayList<Double>();
    final ArrayList<Double>  source = new ArrayList<Double>();
    boolean statusOfGPS;
    GPSTracker gpsLocation ;
    protected double latitudeGPS = 0;
    protected double longitudeGPS = 0;
    private String getReference ;
    private Button saveBtn;
    protected  String _nameReference;
    protected  String _nameUser;
    protected  String _phonenumber;
    protected  String getCurrentLocationName;
    protected  String getDestinationLocationName;
    private String sourceDetail;
    private String destinationDetail;
    public PassengerMapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //saveLocation Button
        saveBtn = (Button) findViewById(R.id.saveLocation);
        saveBtn.setText("Serch");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (sourceDetail == null){
                Toast.makeText(getApplicationContext(),
                        "Please enter pickup location", Toast.LENGTH_LONG).show();
            }else if (destinationDetail == null){
                Toast.makeText(getApplicationContext(),
                        "Please enter destination location", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(Intent.ACTION_CALL);
                if( destination.get(0) != null && source.get(0) !=null) {
                    String getLatitudeDestination = destination.get(0).toString();
                    String getLongitudeDestination = destination.get(1).toString();
                    intent.putExtra("SetLatitudeDestination", getLatitudeDestination);
                    intent.putExtra("SetLongitudeDestination", getLongitudeDestination);

                    String getLatitudeSource = source.get(0).toString();
                    String getLongitudeSource = source.get(1).toString();
                    intent.putExtra("SetLatitudeSource", getLatitudeSource);
                    intent.putExtra("SetLongitudeSource", getLongitudeSource);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)     ;
                intent.setClass(getApplicationContext(), FindDriver.class);
                startActivity(intent);
                finish();

            }
            }
        });


        // Getting a reference to the AutoCompleteTextView
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);
        atvPlaces2 = (AutoCompleteTextView) findViewById(R.id.atv_places2);
        atvPlaces2.setThreshold(1);

        checkEnableGPS();
        // Adding textchange listener
        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Creating a DownloadTask to download Google Places matching "s"
                //  placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                //  String url = getAutoCompleteUrl(s.toString().toLowerCase().trim());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                //  placesDownloadTask.execute(url);

                if(placesDownloadTask!=null)
                {
                    if(placesDownloadTask.getStatus() == AsyncTask.Status.PENDING ||
                            placesDownloadTask.getStatus() == AsyncTask.Status.RUNNING ||
                            placesDownloadTask.getStatus() == AsyncTask.Status.FINISHED)
                    {
                        Log.i("--placesDownloadTask--","progress_status : "+placesDownloadTask.getStatus());
                        placesDownloadTask.cancel(true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String chterm;
                chterm = atvPlaces.getText().toString();
                Log.i("---final selected text---", ""+chterm);
                placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }
        });

        // Adding text change listener two
        atvPlaces2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Creating a DownloadTask to download Google Places matching "s"
                //  placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                //  String url = getAutoCompleteUrl(s.toString().toLowerCase().trim());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                //  placesDownloadTask.execute(url);

                if (placesDownloadTask != null) {
                    if (placesDownloadTask.getStatus() == AsyncTask.Status.PENDING ||
                            placesDownloadTask.getStatus() == AsyncTask.Status.RUNNING ||
                            placesDownloadTask.getStatus() == AsyncTask.Status.FINISHED) {
                        Log.i("--placesDownloadTask--", "progress_status : " + placesDownloadTask.getStatus());
                        placesDownloadTask.cancel(true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String chterm;
                chterm = atvPlaces2.getText().toString();
                Log.i("---final selected text---", "" + chterm);
                placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }
        });


        // Setting an item click listener for the AutoCompleteTextView dropdown list
        atvPlaces.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {
                atvPlacesChecking = true;
                atvPlacesChecking2 = false;
                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);
                sourceDetail = hm.get("description");
                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);

            }
        });

        // Setting an item click listener for the AutoCompleteTextView dropdown list2
        atvPlaces2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {
                atvPlacesChecking = false;
                atvPlacesChecking2 = true;
                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);
                destinationDetail = hm.get("description");

                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void checkEnableGPS() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusOfGPS == true) {
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            setUpMapIfNeeded();
        }
        else {
            showGPSDisabledAlertToUser();
            setUpMapIfNeeded();
        }
    }






    protected  void setUpMapIfNeeded() {
        gpsLocation = new GPSTracker(this);
        latitudeGPS = (float) gpsLocation.getLatitude();
        longitudeGPS = (float) gpsLocation.getLongitude();
        if(googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            checkMyLocation();
            googleMap.setMyLocationEnabled(true);
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeGPS, longitudeGPS), 13));
                checkMyLocation();


            }
        }
    }
    private void checkMyLocation() {

        if (latitudeGPS != 0 && longitudeGPS != 0) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitudeGPS, longitudeGPS)).title("I'm live in Here MyLocation at Latitude  " + latitudeGPS + " and longitude  " + longitudeGPS));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeGPS, longitudeGPS), 13));
        }

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Settings GPS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        onResume();

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }



    private String getAutoCompleteUrl(String place){

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDkptSTTbdpJUuVfCsdXMt08eOc-R__6dw";

        // place to be be searched
        String input = "input="+place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input+ "&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        //https://maps.googleapis.com/maps/api/place/autocomplete/json?input=เซนทรัลพระราม2&sensor=false&key=AIzaSyDkptSTTbdpJUuVfCsdXMt08eOc-R__6dw
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

        return url.trim();
    }

    private String getPlaceDetailsUrl(String ref){

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDkptSTTbdpJUuVfCsdXMt08eOc-R__6dw";

        getReference = ref;
        // reference of place
        String reference = "reference="+ getReference;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference+"&"+key;
        //String parameters = reference+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service

        //https://maps.googleapis.com/maps/api/place/details/json?sensor=reference=false&key=AIzaSyDkptSTTbdpJUuVfCsdXMt08eOc-R__6dw
        //https://maps.googleapis.com/maps/api/place/details/json?reference=CjQiAAAAilke1xA2opfmIfS3AKfqagqzG8bCnq64bF6b5bzCfCWTdMxmwg3go3JxRCKjHjeREhD2yUqt36-3xZB-UPIbfJpqGhRveGbuhCAvEYTp4sLUUlhODO2Gtw&key=AIzaSyDkptSTTbdpJUuVfCsdXMt08eOc-R__6dw

        String url = "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;

        return url.trim();
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }





    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        private int downloadType=0;

        // Constructor
        public DownloadTask(int type){
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch(downloadType){
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS :
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }

    /** A class to parse the Google Places in JSON format */
    protected class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
        int parserType = 0;
        double latitudeStart;
        double longitudeStart;
        double latitudeDestination;
        double longitudeDestination;


        public ParserTask(int type){
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try{
                jObject = new JSONObject(jsonData[0]);

                switch(parserType){
                    case PLACES :
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS :
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch(parserType){
                case PLACES :
                    String[] from = new String[] { "description"};
                    int[] to = new int[] { android.R.id.text1 };

                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                    // Setting the adapter
                    atvPlaces.setAdapter(adapter);
                    atvPlaces2.setAdapter(adapter);
                    break;
                case PLACES_DETAILS :
                    HashMap<String, String> hm = result.get(0);



                    if( atvPlacesChecking == true) {

                        // Getting latitude from the parsed data
                        latitudeStart = Double.parseDouble(hm.get("lat"));
                        // Getting longitude from the parsed data
                        longitudeStart = Double.parseDouble(hm.get("lng"));

                        // Getting reference to the SupportMapFragment of the activity_main.xml
                        // Getting GoogleMap from SupportMapFragment

                        if (markerList.size() > 0) {

                            markerList.get(0).remove();
                            markerList.remove(0);
                        }

                        if ( source.size() >0){
                            source.remove(0);
                            source.remove(1);
                            source.clear();

                        }

                        atvPlacesChecking = false;
                        LatLng point = new LatLng(latitudeStart, longitudeStart);
                        String name = "start";
                        MarkerOptions options = new MarkerOptions().title(name);
                        options.position(point);
                        options.snippet("Latitude:" + latitudeStart + ",Longitude:" + longitudeStart);
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) ;
                        // Adding the marker in the Google Map
                        Marker marker = googleMap.addMarker(options);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeStart, longitudeStart), 13));
                        markerList.add(marker);
                        markerList.contains(marker);
                        source.add((double) latitudeStart);
                        source.add((double) longitudeStart);
                    }
                    else if (atvPlacesChecking2 == true){

                        // Getting latitude from the parsed data
                        latitudeDestination = Double.parseDouble(hm.get("lat"));
                        // Getting longitude from the parsed data
                        longitudeDestination = Double.parseDouble(hm.get("lng"));

                        // Getting reference to the SupportMapFragment of the activity_main.xml
                        // Getting GoogleMap from SupportMapFragment

                        if (markerList2.size() > 0) {

                            markerList2.get(0).remove();
                            markerList2.remove(0);
                        }

                        if ( destination.size() >0){
                            destination.remove(0);
                            destination.remove(1);
                            destination.clear();

                        }

                        atvPlacesChecking2 = false;
                        LatLng point = new LatLng(latitudeDestination, longitudeDestination);
                        String name = "start";
                        MarkerOptions options = new MarkerOptions().title(name);
                        options.position(point);
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)) ;
                        options.snippet("Latitude:" + latitudeDestination + ",Longitude:" + longitudeDestination);
                        destination.add((double) latitudeDestination);
                        destination.add((double) longitudeDestination);
                        // Adding the marker in the Google Map
                        Marker marker = googleMap.addMarker(options);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudeDestination, longitudeDestination), 13));
                        markerList2.add(marker);
                        markerList2.contains(marker);
                    }



            }
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}