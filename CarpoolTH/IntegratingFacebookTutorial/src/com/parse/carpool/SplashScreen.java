package com.parse.carpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.integratingfacebooktutorial.R;

/**
 * Created by JUMRUS on 23/8/2558.
 */
public class SplashScreen extends Activity {

    private Handler myHandler;

    static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash_screen);

        myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent goMain = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(goMain);
            }
        }, 5000);

    }

}
