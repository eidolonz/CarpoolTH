package com.parse.carpool;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class IntegratingFacebook extends Application {

  static final String TAG = "MyApp";

  @Override
  public void onCreate() {
    super.onCreate();

      FacebookSdk.sdkInitialize(getApplicationContext());

      Parse.initialize(this,
              "LeZCvTlDl3haYiWuS0AxMoZyGgKVumut8DCO4sbC",
              "ayeEhf9aiFlRteeI94sYyK61p969iWQaW4iocStX"
      );

      ParseFacebookUtils.initialize(this);
  }
}
