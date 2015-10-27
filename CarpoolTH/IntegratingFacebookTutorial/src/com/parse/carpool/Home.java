package com.parse.carpool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by JUMRUS on 12/9/2558.
 */
public class Home extends Fragment {
    private static View view;
    private static TextView tv1;
    private String name = "Null";
    List<ParseObject> ob;
    ListView listView ;
    List<Trip> trip;
    String creatorName;

    private Button showActBtn;
    private boolean isActShow;


    protected ParseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view = inflater.inflate(R.layout.home,container,false);
        }catch (InflateException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        tv1 = (TextView) view.findViewById(R.id.textView1);



        // Get ListView object from xml
        listView = (ListView) view.findViewById(com.parse.integratingfacebooktutorial.R.id.driverList);

        // Create the array
        trip = new ArrayList<Trip>();
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CreateTrip");
            ParseUser currentUser = ParseUser.getCurrentUser();

            ob = query.find();
            for (ParseObject trips : ob) {
                ParseObject userPointer = trips.getParseObject("CreateBy");
                String userId = userPointer.getObjectId();

                if( currentUser.getObjectId().equals(userId) ) {
                    Trip tripDetail = new Trip();

                    tripDetail.setSource((String) trips.get("Source"));
                    tripDetail.setDestination((String) trips.get("Destination"));
                    //tripDetail.setCreateBy(trips.get("CreateBy"));
                    ParseObject parseObject = (ParseObject)trips.get("CreateBy");

                    tripDetail.setName(getCreator(parseObject));
                    tripDetail.setObjectId(trips.getObjectId());
                    //tripDetail.setCreateBy((ParseObject) trips.get("CreateBy"));
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

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), trip);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                // Show Alert
                Toast.makeText(getActivity(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), TripDetail.class);
                startActivity(intent);


            }

        });

        listView.setVisibility(ListView.GONE);
        this.isActShow = false;

        showActBtn = (Button) view.findViewById(R.id.showActBtn);
        showActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isActShow) {
                    listView.setVisibility(ListView.VISIBLE);
                    isActShow = true;
                    showActBtn.setText("Hide Your Activities");
                } else {
                    listView.setVisibility(ListView.GONE);
                    isActShow = false;
                    showActBtn.setText("Show Your Activities");
                }
            }
        });
        setText();
        return view;
    }

    private String getCreator(ParseObject parseObject){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", parseObject.getObjectId().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : parseObjects) {
                        // This does not require a network access.
                        creatorName = object.getString("name");
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                    creatorName = " Not found Name";
                }
            }
        });
        return creatorName;
    }

    public void setText(){
        currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", currentUser.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : parseObjects) {
                        // This does not require a network access.
                        name = object.getString("name");
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                    name = " Not found Name";
                    tv1.setText(name);
                }
                tv1.setText(name);
            }
        });

    }


}
