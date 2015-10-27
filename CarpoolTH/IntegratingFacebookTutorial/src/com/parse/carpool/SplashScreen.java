package com.parse.carpool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.integratingfacebooktutorial.R;

/**
 * Created by JUMRUS on 23/8/2558.
 */
public class SplashScreen extends AppCompatActivity {

    private Handler myHandler;
/*
    GoogleMap googleMap;
    boolean statusOfGPS;
    GPSTracker gpsLocation ;
    protected double latitudeGPS = 0;
    protected double longitudeGPS = 0;
*/
    static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash_screen);

        myHandler = new Handler();

        //checkEnableGPS();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent goMain = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(goMain);
            }
        }, 5000);

    }

/*
    private void checkEnableGPS() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusOfGPS == true) {
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            //setUpMapIfNeeded();
        }
        else {
            showGPSDisabledAlertToUser();
            //setUpMapIfNeeded();
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
*/
}
