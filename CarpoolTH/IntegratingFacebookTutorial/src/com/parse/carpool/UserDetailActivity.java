package com.parse.carpool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.integratingfacebooktutorial.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JUMRUS on 25/10/2558.
 */
public class UserDetailActivity extends AppCompatActivity {

    public List<ParseObject> ob;
    public List<User> user = new ArrayList<User>();
    public User userDetail = new User();

    private ProfilePictureView userProfilePictureView;
    private TextView userNameView;
    private TextView userGenderView;
    private TextView userEmailView;

    private String userObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetails);

        Intent i = getIntent();
        userObjectId = i.getStringExtra("ObjectId");

        getUserDetail();
        setView();

    }
    public void getUserDetail() {
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
            query.whereEqualTo("objectId", userObjectId);
            ob = query.find();
            for (ParseObject users : ob) {
                userDetail.setObjectId(users.getObjectId());
                userDetail.setName(users.getString("name"));
                userDetail.setEmail(users.getString("email"));
                userDetail.setMobileNo(users.getString("mobileNo"));
                userDetail.setGender(users.getString("gender"));

                user.add(userDetail);}
            }
        catch(ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
        }
    }

    public void setView(){
        this.userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        this.userNameView = (TextView)  findViewById(R.id.userName);
        this.userGenderView = (TextView)  findViewById(R.id.userGender);
        this.userEmailView = (TextView)  findViewById(R.id.userEmail);

        HashMap id = (HashMap) userDetail.getProfile();
        this.userProfilePictureView.setProfileId(id.get("facebookId").toString());
        this.userNameView.setText(userDetail.getName());
        this.userGenderView.setText(userDetail.getGender());
        this.userEmailView.setText(userDetail.getEmail());
    }



}
