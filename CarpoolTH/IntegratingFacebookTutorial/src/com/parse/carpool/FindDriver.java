package com.parse.carpool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUMRUS on 12/9/2558.
 */
public class FindDriver extends ActionBarActivity {
    public static final double R = 6372.8;
    private Dialog progressDialog;
    List<ParseObject> ob;
    List<Trip> trip;
    ListView listView ;
    public String name;
    double latDestination1;
    double lonDestination1;
    double latSource1;
    double lonSource1;
    double latDestination2;
    double lonDestination2;
    double latSource2;
    double lonSource2;
    int route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.parse.integratingfacebooktutorial.R.layout.driver_listview);
        Intent i = getIntent();
        latDestination2 = Double.parseDouble(i.getStringExtra("SetLatitudeDestination"));
        lonDestination2 = Double.parseDouble(i.getStringExtra("SetLongitudeDestination"));
        latSource2 = Double.parseDouble(i.getStringExtra("SetLatitudeSource"));
        lonSource2 = Double.parseDouble(i.getStringExtra("SetLongitudeSource"));
        route = Integer.parseInt(i.getStringExtra("Route"));

        // Get ListView object from xml
        listView = (ListView) findViewById(com.parse.integratingfacebooktutorial.R.id.driverList);


        // Create the array
         trip = new ArrayList<Trip>();
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CreateTrip");


            query.orderByAscending("TripId");
            ob = query.find();
            for (ParseObject trips : ob) {
                // Locate images in flag column
                //ParseFile image = (ParseFile) country.get("flag");

                double latDestination1 =  Double.parseDouble((String) trips.get("LatitudeDestination"));
                double lonDestination1 =  Double.parseDouble((String) trips.get("LongitudeDestination"));
                double latSource1 =  Double.parseDouble((String) trips.get("LatitudeSource"));
                double lonSource1 =  Double.parseDouble((String) trips.get("LongitudeSource"));
                int route1 = Integer.parseInt((String)trips.get("Route")) ;


                if (haveRsine(latDestination1,lonDestination1,latDestination2,lonDestination2) <= 2
                        && haveRsine(latSource1,lonSource1,latSource2,lonSource2) <= 2
                        && route == route1) {

                    Trip tripDetail = new Trip();

                    tripDetail.setSource((String) trips.get("Source"));
                    tripDetail.setDestination((String) trips.get("Destination"));
                    tripDetail.setCreateBy(trips.getParseObject("CreateBy"));
                    tripDetail.setObjectId(trips.getObjectId());
                    //tripDetail.setCreateBy((JSONObject) trips.get("CreateBy"));
                    trip.add(tripDetail);
                }
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ListViewAdapter adapter = new ListViewAdapter(this, trip);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                progressDialog = ProgressDialog.show(FindDriver.this, "", "Loading...", true);

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), TripDetail.class);
                startActivity(intent);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.parse.integratingfacebooktutorial.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case com.parse.integratingfacebooktutorial.R.id.action_search:
                openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSearch(){
        Intent intent = new Intent(this, PassengerMapActivity.class);
        startActivity(intent);
    }

    public  double haveRsine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

}
