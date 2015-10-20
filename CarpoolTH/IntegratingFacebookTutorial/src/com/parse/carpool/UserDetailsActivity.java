package com.parse.carpool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class UserDetailsActivity extends Fragment {

  private static View view;

  private ProfilePictureView userProfilePictureView;
  private TextView userNameView;
  private TextView userGenderView;
  private TextView userEmailView;
  private TextView userAgeView;
  private Button userLogout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (view != null) {
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null)
        parent.removeView(view);
    }
    try{
      view = inflater.inflate(R.layout.userdetails,container,false);
    }catch (InflateException e){
			/* map is already there, just return view as it is */
    }
    userProfilePictureView = (ProfilePictureView) view.findViewById(R.id.userProfilePicture);
    userNameView = (TextView)  view.findViewById(R.id.userName);
    userGenderView = (TextView)  view.findViewById(R.id.userGender);
    userEmailView = (TextView)  view.findViewById(R.id.userEmail);
    userAgeView = (TextView)  view.findViewById(R.id.userAge);
    userLogout  = (Button) view.findViewById(R.id.logoutBtn);
    userLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onLogoutClick(v);
      }
    });


    //Fetch Facebook user info if it is logged
    ParseUser currentUser = ParseUser.getCurrentUser();
    if ((currentUser != null) && currentUser.isAuthenticated()) {
      makeMeRequest();
    }
    return view;

  }

  @Override
  public void onResume() {
    super.onResume();

    ParseUser currentUser = ParseUser.getCurrentUser();
    if (currentUser != null) {
      // Check if the user is currently logged
      // and show any cached content
      updateViewsWithProfileInfo();
    } else {
      // If the user is not logged in, go to the
      // activity showing the login view.
      startLoginActivity();
    }
  }

  private void makeMeRequest() {
    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
              @Override
              public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                if (jsonObject != null) {
                  JSONObject userProfile = new JSONObject();

                  try {
                    userProfile.put("facebookId", jsonObject.getLong("id"));
                    userProfile.put("name", jsonObject.getString("name"));

                    // Save the user profile info in a user property
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.put("profile", userProfile);
                    currentUser.saveInBackground();

                    //This code for saving data to another table
								/*
								ParseObject dataObject = new ParseObject("CreateTrip");
								dataObject.put("TripDetail", userProfile);
								dataObject.saveInBackground();*/

                    currentUser.put("name", userProfile.get("name"));
                    currentUser.saveInBackground();

                    // Show the user info
                    updateViewsWithProfileInfo();
                  } catch (JSONException e) {
                    Log.d(IntegratingFacebook.TAG,
                            "Error parsing returned user data. " + e);
                  }
                } else if (graphResponse.getError() != null) {
                  switch (graphResponse.getError().getCategory()) {
                    case LOGIN_RECOVERABLE:
                      Log.d(IntegratingFacebook.TAG,
                              "Authentication error: " + graphResponse.getError());
                      break;

                    case TRANSIENT:
                      Log.d(IntegratingFacebook.TAG,
                              "Transient error. Try again. " + graphResponse.getError());
                      break;

                    case OTHER:
                      Log.d(IntegratingFacebook.TAG,
                              "Some other error: " + graphResponse.getError());
                      break;
                  }
                }
              }
            });

    request.executeAsync();
  }

  private void updateViewsWithProfileInfo() {
    ParseUser currentUser = ParseUser.getCurrentUser();
    currentUser.fetchInBackground();
    if (currentUser.has("profile")) {
      JSONObject userProfile = currentUser.getJSONObject("profile");
      try {

        if (userProfile.has("facebookId")) {
          userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
        } else {
          // Show the default, blank user profile picture
          userProfilePictureView.setProfileId(null);
        }

        if (userProfile.has("name")) {
          userNameView.setText(userProfile.getString("name"));
        } else {
          userNameView.setText("");
        }

        /*
        if (userProfile.has("email")) {
          userEmailView.setText(userProfile.getString("email"));
        } else {
          userEmailView.setText("no mail");
        }

        if (userProfile.has("gender")) {
          userGenderView.setText(userProfile.getString("gender"));
        } else {
          userGenderView.setText("no gen");
        }*/
        String a = currentUser.getString("email");
        String b = currentUser.getString("gender");
        userEmailView.setText(currentUser.getString("email"));
        userGenderView.setText(currentUser.getString("gender"));


      } catch (JSONException e) {
        Log.d(IntegratingFacebook.TAG, "Error parsing saved user data.");
      }
    }
  }

  public void onLogoutClick(View v) {
    logout();
  }

  private void logout() {
    // Log the user out
    ParseUser.logOut();

    // Go to the login view
    startLoginActivity();
  }

  private void startLoginActivity() {
    Intent intent = new Intent(getActivity(), LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }
}
