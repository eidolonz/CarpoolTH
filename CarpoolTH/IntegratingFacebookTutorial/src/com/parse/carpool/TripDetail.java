package com.parse.carpool;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JUMRUS on 17/9/2558.
 */
public class TripDetail extends AppCompatActivity {
    public List<ParseObject> ob;
    public List<Trip> trip = new ArrayList<Trip>();
    public Trip tripDetail = new Trip();

    ParseUser currentUser;

    protected String userId;
    protected String passengerName;

    LinearLayout travelDaily;
    LinearLayout travelOn;
    ScrollView sv_container;

    RoundImage roundImage;
    ImageView sun;
    ImageView mon;
    ImageView tue;
    ImageView wed;
    ImageView thu;
    ImageView fri;
    ImageView sat;
    TextView selectedDate;
    TextView name;
    TextView sourceTV;
    TextView destinationTV;
    TextView money;
    TextView passenger;
    TextView ciga;
    TextView car;
    TextView tel;
    TextView mail;
    TextView description;
    TableRow passenger1;
    TableRow passenger2;
    TableRow passenger3;
    TableRow passenger4;
    TableRow passenger5;
    TableRow passenger6;
    TextView passengerName1;
    TextView passengerName2;
    TextView passengerName3;
    TextView passengerName4;
    TextView passengerName5;
    TextView passengerName6;
    TextView tvStartTime;
    TextView tvReturnTime;
    TextView tvTravelDate;
    ImageView icnFacebook;
    ImageView icnPhone;
    Button joinBtn;
    Button cancelBtn;
    Button deleteBtn;
    Button chatBtn;

    GoogleDirection gd;
    Document mDoc;
    GoogleMap googleMap;

    private String tripId;
    private ProfilePictureView userProfilePictureView;
    private ProfilePictureView co_tripperImg01;
    private ProfilePictureView co_tripperImg02;
    private ProfilePictureView co_tripperImg03;
    private ProfilePictureView co_tripperImg04;
    private ProfilePictureView co_tripperImg05;
    private ProfilePictureView co_tripperImg06;
    private boolean isJoin;
    private int position;
    private boolean isFull;
    private boolean isOwner;
    private double latSource = 0.00;
    private double longSource = 0.00;
    private double latDes = 0.00;
    private double longDes = 0.00;
    private boolean isRun = false;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_detail);

        Intent i = getIntent();
        tripId = i.getStringExtra("TripId");

        this.isJoin = false;
        this.isFull = false;
        this.isOwner = false;

        getCurrentDetail();
        getTripDetail();


        sv_container = (ScrollView)findViewById(R.id.sv_container);


        joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinTrip();
            }
        });

        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(Button.GONE);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTrip();
            }
        });

        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setVisibility(Button.GONE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrip(false);
            }
        });

        if(isFull && !isJoin){
            joinBtn.setClickable(false);
            joinBtn.setBackgroundColor(Color.LTGRAY);
            joinBtn.setText("TRIP FULL");
        }
        if(isJoin){
            joinBtn.setVisibility(Button.GONE);
            cancelBtn.setVisibility(Button.VISIBLE);
        }


        ParseObject userPointer = tripDetail.getCreateBy();
        if(userPointer != null) {
            userId = userPointer.getObjectId();
        }else{
            Toast.makeText(this,
                    "This trip is missing!", Toast.LENGTH_LONG).show();
            backToMain();
        }

        if( currentUser.getObjectId().equals(userId) ) {
            this.isOwner = true;
            joinBtn.setVisibility(Button.GONE);
            cancelBtn.setVisibility(Button.GONE);
            deleteBtn.setVisibility(Button.VISIBLE);
        }


        name = (TextView) findViewById(R.id.name);
        sourceTV = (TextView) findViewById(R.id.source);
        destinationTV = (TextView) findViewById(R.id.destination);
        ciga = (TextView) findViewById(R.id.ciga);
        money = (TextView) findViewById(R.id.money_TV);
        car = (TextView) findViewById(R.id.carType);
        tel = (TextView) findViewById(R.id.tel);
        mail = (TextView) findViewById(R.id.email);
        description = (TextView) findViewById(R.id.description);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvReturnTime = (TextView) findViewById(R.id.tvReturnTime);
        tvTravelDate = (TextView) findViewById(R.id.tvTravelDate);
        icnFacebook = (ImageView) findViewById(R.id.icnFacebook);
        icnPhone = (ImageView) findViewById(R.id.icnPhone);

        icnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFacebook(tripDetail.getFacebookId());
            }
        });
        icnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(tripDetail.getPhoneNo());
            }
        });
        name.setText(tripDetail.getName());
        sourceTV.setText(tripDetail.getSource());
        destinationTV.setText(tripDetail.getDestination());
        if(tripDetail.isSmoke()) {
            ciga.setText("Not Allowed");
        }else{
            ciga.setText("Doesn't Matter");
        }
        String moneyText = tripDetail.getMoney() + " บาท/คน";
        money.setText(moneyText);
        car.setText(tripDetail.getCar());
        tel.setText(tripDetail.getPhoneNo());
        mail.setText(tripDetail.getEmail());
        description.setText(tripDetail.getDescription());
        if(tripDetail.getStartHour() > 11){
            tvStartTime.setText((tripDetail.getStartHour()-12) + ":" + tripDetail.getStartMinute() + " PM");
        }else{
            tvStartTime.setText((tripDetail.getStartHour()) + ":" + tripDetail.getStartMinute() + " AM");
        }
        if (tripDetail.getReturnHour() > 11){
            tvReturnTime.setText((tripDetail.getReturnHour()-12) + ":" + tripDetail.getReturnMinute() + " PM");
        }else{
            tvReturnTime.setText((tripDetail.getReturnHour()) + ":" + tripDetail.getReturnMinute() + " AM");
        }

        userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        //Fetch Facebook user info if it is logged
        updateViewsWithProfileInfo();

        chatBtn = (Button) findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ChatActivity.class);
                intent.putExtra("RoomId", tripDetail.getObjectId());
                String a = tripDetail.getFacebookId();
                intent.putExtra("FbId", tripDetail.getFacebookId());
                startActivity(intent);
            }
        });
        setUpMapIfNeeded();
    }






private void callFacebook(String FBid){
    Intent intent;
    try {
        getApplicationContext().getPackageManager()
                .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
        intent = new Intent(Intent.ACTION_VIEW,
                //Uri.parse("fb://profile/10205925145306860")); //Trys to make intent with FB's URI
                Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/" + FBid));
                //Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/" + FBid));
    } catch (Exception e) {
        intent =  new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/arkverse")); //catches and opens a url to the desired page
    }
    startActivity(intent);
}

private void callPhone(String PhoneNo){
    Uri number = Uri.parse("tel:" + PhoneNo);
    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
    startActivity(callIntent);
}





private void getTripDetail(){

        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CreateTrip");
            query.whereEqualTo("objectId", tripId);
            ob = query.find();
            for (ParseObject trips : ob) {
                tripDetail.setCreateBy(trips.getParseObject("CreateBy"));

                if(trips.get("LatitudeSource") != null && trips.get("LongitudeSource") != null && trips.get("LatitudeDestination") != null && trips.get("LongitudeDestination") != null  ){
                    this.latSource = Double.parseDouble(trips.get("LatitudeSource").toString());
                    this.longSource = Double.parseDouble(trips.get("LongitudeSource").toString());
                    this.latDes = Double.parseDouble(trips.get("LatitudeDestination").toString());
                    this.longDes = Double.parseDouble(trips.get("LongitudeDestination").toString());
                }

                if(trips.get("daily") != null){
                    tripDetail.setWeek(trips.get("daily"));
                    setDaily(true);
                }else{
                    tripDetail.setSelectedDate(trips.getString("selectedDate"));
                    setDaily(false);

                }

                if (trips.get("PassengerId") != null){
                    HashMap hm = (HashMap) trips.get("PassengerId");
                    tripDetail.setPassengerId(hm);
                    /*if(hm != null && hm.size() < 6){
                        joinBtn.setClickable(false);
                    }*/
                    setPassengerId();
                }
                tripDetail.setStartHour((int) trips.get("StartHour"));
                tripDetail.setStartMinute((int) trips.get("StartMinute"));
                tripDetail.setReturnHour((int) trips.get("ReturnHour"));
                tripDetail.setReturnMinute((int) trips.get("ReturnMinute"));
                tripDetail.setFacebookId((Long) trips.get("FacebookId"));
                tripDetail.setName(trips.getString("OwnerName"));
                tripDetail.setSource(trips.getString("Source"));
                tripDetail.setDestination(trips.getString("Destination"));
                tripDetail.setIsSmoke(trips.getBoolean("Smoke"));
                tripDetail.setMoney((int) trips.get("Money"));
                tripDetail.setPassenger((int) trips.get("Passenger"));
                tripDetail.setCar(trips.getString("Car"));
                tripDetail.setPhoneNo(trips.getString("Tel"));
                tripDetail.setObjectId(trips.getObjectId());
                tripDetail.setEmail(trips.getString("Mail"));
                tripDetail.setDescription(trips.getString("Description"));

                trip.add(tripDetail);
            }
/**
            ParseQuery<ParseObject> owner = ParseQuery.getQuery("_User");
            ownerId = ((ParseUser) tripDetail.getCreateBy()).getObjectId();
            owner.getInBackground(ownerId, new GetCallback<ParseObject>() {

                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        tripDetail.setName(parseObject.getString("name"));
                    }
                }
            });*/
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public void setPassengerId(){
        passenger1 = (TableRow) findViewById(R.id.passenger1);
        passenger2 = (TableRow) findViewById(R.id.passenger2);
        passenger3 = (TableRow) findViewById(R.id.passenger3);
        passenger4 = (TableRow) findViewById(R.id.passenger4);
        passenger5 = (TableRow) findViewById(R.id.passenger5);
        passenger6 = (TableRow) findViewById(R.id.passenger6);
        passenger1.setVisibility(TableRow.GONE);
        passenger2.setVisibility(TableRow.GONE);
        passenger3.setVisibility(TableRow.GONE);
        passenger4.setVisibility(TableRow.GONE);
        passenger5.setVisibility(TableRow.GONE);
        passenger6.setVisibility(TableRow.GONE);

        co_tripperImg01 = (ProfilePictureView) findViewById(R.id.co_tripperImg01);
        co_tripperImg02 = (ProfilePictureView) findViewById(R.id.co_tripperImg02);
        co_tripperImg03 = (ProfilePictureView) findViewById(R.id.co_tripperImg03);
        co_tripperImg04 = (ProfilePictureView) findViewById(R.id.co_tripperImg04);
        co_tripperImg05 = (ProfilePictureView) findViewById(R.id.co_tripperImg05);
        co_tripperImg06 = (ProfilePictureView) findViewById(R.id.co_tripperImg06);

        passengerName1 = (TextView) findViewById(R.id.passengerName1);
        passengerName2 = (TextView) findViewById(R.id.passengerName2);
        passengerName3 = (TextView) findViewById(R.id.passengerName3);
        passengerName4 = (TextView) findViewById(R.id.passengerName4);
        passengerName5 = (TextView) findViewById(R.id.passengerName5);
        passengerName6 = (TextView) findViewById(R.id.passengerName6);

        HashMap hm = (HashMap) tripDetail.getPassengerId();
        if (hm!=null) {
            if (hm.size() >= 1) {
                HashMap userDetail = (HashMap) hm.get("PassengerId0");
                co_tripperImg01.setProfileId((String) userDetail.get("FacebookId"));
                passengerName1.setText((String) userDetail.get("Name"));
                setJoin(userDetail,0);
                passenger1.setVisibility(TableRow.VISIBLE);
            }
            if (hm.size() >= 2) {
                HashMap userDetail = (HashMap) hm.get("PassengerId1");
                co_tripperImg02.setProfileId((String)userDetail.get("FacebookId"));
                passengerName2.setText((String) userDetail.get("Name"));
                setJoin(userDetail,1);
                passenger2.setVisibility(TableRow.VISIBLE);
            }
            if (hm.size() >= 3) {
                HashMap userDetail = (HashMap) hm.get("PassengerId2");
                co_tripperImg03.setProfileId((String)userDetail.get("FacebookId"));
                passengerName3.setText((String) userDetail.get("Name"));
                setJoin(userDetail,2);
                passenger3.setVisibility(TableRow.VISIBLE);
            }
            if (hm.size() >= 4) {
                HashMap userDetail = (HashMap) hm.get("PassengerId3");
                co_tripperImg04.setProfileId((String)userDetail.get("FacebookId"));
                passengerName4.setText((String) userDetail.get("Name"));
                setJoin(userDetail,3);
                passenger4.setVisibility(TableRow.VISIBLE);
            }
            if (hm.size() >= 5) {
                HashMap userDetail = (HashMap) hm.get("PassengerId4");
                co_tripperImg05.setProfileId((String)userDetail.get("FacebookId"));
                passengerName5.setText((String) userDetail.get("Name"));
                setJoin(userDetail,4);
                passenger5.setVisibility(TableRow.VISIBLE);
            }
            if (hm.size() >= 6) {
                HashMap userDetail = (HashMap) hm.get("PassengerId5");
                co_tripperImg06.setProfileId((String)userDetail.get("FacebookId"));
                passengerName6.setText((String) userDetail.get("Name"));
                setJoin(userDetail, 5);
                passenger6.setVisibility(TableRow.VISIBLE);
                isFull = true;
            }
        }

    }

    public void setJoin(HashMap userDetail, int position){
        String fbId =  (String)userDetail.get("FacebookId");
        if( fbId.equals(this.userId)){
            this.isJoin = true;
            this.position = position;
        }
    }
    public void setDaily(boolean isDaily){
        travelDaily = (LinearLayout) findViewById(R.id.travel_daily);
        travelOn = (LinearLayout) findViewById(R.id.travel_on);

        if(isDaily){
            sun = (ImageView) findViewById(R.id.sun);
            mon = (ImageView) findViewById(R.id.mon);
            tue = (ImageView) findViewById(R.id.tue);
            wed = (ImageView) findViewById(R.id.wed);
            thu = (ImageView) findViewById(R.id.thu);
            fri = (ImageView) findViewById(R.id.fri);
            sat = (ImageView) findViewById(R.id.sat);

            HashMap hash = (HashMap) tripDetail.getWeek();
            boolean su = (boolean) hash.get("sun");
            boolean mo = (boolean) hash.get("mon");
            boolean tu = (boolean) hash.get("tue");
            boolean we = (boolean) hash.get("wed");
            boolean th = (boolean) hash.get("thu");
            boolean fr = (boolean) hash.get("fri");
            boolean sa = (boolean) hash.get("sat");
            if (su){
                sun.setImageResource(R.drawable.sun01);
            }
            if (mo){
                mon.setImageResource(R.drawable.mon01);
            }
            if (tu){
                tue.setImageResource(R.drawable.tue01);
            }
            if (we){
                wed.setImageResource(R.drawable.wed01);
            }
            if (th){
                thu.setImageResource(R.drawable.thu01);
            }
            if (fr){
                fri.setImageResource(R.drawable.fri01);
            }
            if (sa){
                sat.setImageResource(R.drawable.sat01);
            }




            travelDaily.setVisibility(LinearLayout.VISIBLE);
            travelOn.setVisibility(LinearLayout.GONE);
        }else{
            selectedDate = (TextView) findViewById(R.id.selectedDate);
            selectedDate.setText(tripDetail.getSelectedDate());

            travelDaily.setVisibility(LinearLayout.GONE);
            travelOn.setVisibility(LinearLayout.VISIBLE);
        }

    }

    public void getCurrentDetail(){
        currentUser = ParseUser.getCurrentUser();
        JSONObject userProfile = currentUser.getJSONObject("profile");
        try {
            this.userId = userProfile.getString("facebookId");
            this.passengerName = userProfile.getString("name");
        }catch (JSONException e){}
    }

    public void cancelTrip(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("CreateTrip");
        query.getInBackground(tripDetail.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    HashMap hm = (HashMap) tripDetail.getPassengerId();

                    while (hm.size() >= position && position < 6) {
                        int nextPos = position + 1;
                        if (nextPos == hm.size()) {
                            hm.remove("PassengerId" + position);
                        } else {
                            hm.put("PassengerId" + position, hm.get("PassengerId" + nextPos));
                            hm.remove("PassengerId" + nextPos);
                        }
                        position++;
                    }
                    object.put("PassengerId", hm);
                    object.saveInBackground();
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                final ParseQuery<ParseObject> user = ParseQuery.getQuery("_User");
                user.getInBackground(currentUser.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            object.remove("Trip");
                            object.saveInBackground();
                        }
                    }
                });
            }

        });
        backToMain();
    }

    public void deleteTrip(boolean confirm){
        if(!confirm) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to delete this trip?\n" +
                    "There is no undo!");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteTrip(true);
                        }
                    });
            builder1.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else{
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("CreateTrip");
            query.getInBackground(tripDetail.getObjectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if (e == null) {
                        try {
                            object.delete();
                            object.saveInBackground();
                        } catch (Exception ex) {
                        }
                    }
                }

            });
            backToMain();
        }
    }

    public void joinTrip() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("CreateTrip");
        query.getInBackground(tripDetail.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    HashMap hm = (HashMap) tripDetail.getPassengerId();
                    HashMap userDetail = new HashMap();
                    if (hm == null) {
                        hm = new HashMap();
                    }

                    userDetail.put("FacebookId", userId);
                    userDetail.put("UserId", currentUser);
                    userDetail.put("Name", passengerName);


                    hm.put("PassengerId" + hm.size(), userDetail);


                    object.put("PassengerId", hm);
                    object.saveInBackground();
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                final ParseQuery<ParseObject> user = ParseQuery.getQuery("_User");
                user.getInBackground(currentUser.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            object.put("Trip", tripDetail.getObjectId());
                            object.saveInBackground();
                        }
                    }
                });
            }
        });

        backToMain();

    }

    public void backToMain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void updateViewsWithProfileInfo() {
        userProfilePictureView.setProfileId(tripDetail.getFacebookId());
        /*
        ParseUser currentUser = ParseUser.getCurrentUser();
        this.userId = currentUser.getObjectId();
        if (currentUser.has("profile")) {
            JSONObject userProfile = currentUser.getJSONObject("profile");
            try {

                if (userProfile.has("facebookId")) {
                    userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
                } else {
                    // Show the default, blank user profile picture
                    userProfilePictureView.setProfileId(null);
                }

            } catch (JSONException e) {
                Log.d(IntegratingFacebook.TAG, "Error parsing saved user data.");
            }

        }*/

    }

    protected  void setUpMapIfNeeded() {
        if(googleMap == null) {

            googleMap = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    sv_container.requestDisallowInterceptTouchEvent(true);
                }
            });

            googleMap.setMyLocationEnabled(true);
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.65098, 100.491611), 13));
                gd = new GoogleDirection(this);

                gd.setLogging(true);
                gd.request(new LatLng(latSource, longSource), new LatLng(latDes, longDes), GoogleDirection.MODE_DRIVING);
                gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
                    public void onResponse(String status, Document doc, GoogleDirection gd) {
                        mDoc = doc;
                        googleMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latSource, longSource))
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN)));

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latDes, longDes))
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_GREEN)));
                        if(!isRun) {
                            gd.request(new LatLng(latSource, longSource), new LatLng(latDes, longDes), GoogleDirection.MODE_DRIVING);
                            isRun = true;
                        }

                    }
                });

            }
        }
        googleMap.setMyLocationEnabled(true);

    }


}


