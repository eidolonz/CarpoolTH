package com.parse.carpool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.integratingfacebooktutorial.R;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Button reviewBtn;
    private RatingBar ratingReview;
    private TextView textReview;

    private String userObjectId;
    private String userFacebookId;
    private String currentUserId;
    private String currentUserFbId;
    private ArrayList<Review> mReviews;
    private ReviewListAdapter mAdapter;
    private ListView lvReview;
    private boolean mFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_review);

        Intent i = getIntent();
        userObjectId = i.getStringExtra("user");
        userFacebookId = i.getStringExtra("fbId");

        getUserDetail();
        setView();
        setupReviewPosting();
        getReview();

    }

    private void setupReviewPosting(){
        lvReview = (ListView)findViewById(R.id.lvReview);
        mReviews = new ArrayList<Review>();
        mAdapter = new ReviewListAdapter(UserDetailActivity.this, userObjectId, mReviews);
        lvReview.setAdapter(mAdapter);
    }
    public void getUserDetail() {

        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
            query.whereEqualTo("objectId", userObjectId);
            ob = query.find();
            for (ParseObject users : ob) {
                userDetail.setTotalRating((float)users.getDouble("totalRating"));
                userDetail.setRatingCounter(users.getInt("ratingCounter"));
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
        this.reviewBtn = (Button) findViewById(R.id.reviewBtn);
        this.ratingReview = (RatingBar) findViewById(R.id.ratingBar);

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });



        this.userProfilePictureView.setProfileId(userFacebookId);
        this.userNameView.setText(userDetail.getName());
        this.userGenderView.setText(userDetail.getGender());
        this.userEmailView.setText(userDetail.getEmail());
        this.ratingReview.setRating(userDetail.getTotalRating() / userDetail.getRatingCounter());
    }

    private void createDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(UserDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_dialog, null);
        this.ratingReview = (RatingBar) view.findViewById(R.id.ratingReview);
        this.textReview = (TextView) view.findViewById(R.id.textReview);
        builder.setView(view);

        final EditText textReview = (EditText) view.findViewById(R.id.textReview);
        getCurrentUser();

        builder.setPositiveButton("Review", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check username password
                if (!(textReview.getText().equals(""))) {
                    addReview(ratingReview.getRating(), textReview.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
    private void getReview(){
        // Construct query to execute
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        // Configure limit and sort order
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.whereEqualTo("userId", userObjectId);
        query.findInBackground(new FindCallback<Review>() {
            public void done(List<Review> reviews, ParseException e) {
                if (e == null) {
                    mReviews.clear();
                    mReviews.addAll(reviews);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvReview.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void addReview(double rating, String text){
        Review review  = new Review();
        review.setRating(rating);
        review.setTotalRating(review.getTotalRating() + rating);
        review.setRatingCounter(review.getRatingCounter() + 1);
        review.setUserId(userObjectId);
        review.setTextReview(text);
        review.setRater(this.currentUserId);
        review.setUserFbId(this.currentUserFbId);
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
            query.whereEqualTo("objectId", userObjectId);
            ob = query.find();
            for (ParseObject users : ob) {
                userDetail.setTotalRating((float)(users.getDouble("totalRating")+ review.getRating()));
                userDetail.setRatingCounter(users.getInt("ratingCounter") + 1);

                user.add(userDetail);}
        }
        catch(ParseException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        final ParseQuery<ParseObject> user = ParseQuery.getQuery("_User");
        user.getInBackground(userObjectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("totalRating", userDetail.getTotalRating());
                    parseObject.put("ratingCounter", userDetail.getRatingCounter());
                    parseObject.saveInBackground();
                }
            }
        });

        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                getReview();
            }
        });
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        final ParseQuery<ParseObject> user = ParseQuery.getQuery("_User");
//        user.getInBackground(currentUser.getObjectId(), new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject parseObject, ParseException e) {
//
//                if (e == null) {
//                    parseObject.put("totalRating", mail);
//                    parseObject.put("ratingCounter", phone);
//                    parseObject.saveInBackground();
//                }
//            }
//        });



    }

    private void addToUser(){
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
            query.whereEqualTo("objectId", userObjectId);
            ob = query.find();
            for (ParseObject users : ob) {
                userDetail.setTotalRating((float) users.getDouble("totalRating"));
                userDetail.setRatingCounter(users.getInt("ratingCounter"));
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

    private void getCurrentUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
//        this.currentUserId = currentUser.getObjectId();
        JSONObject jsonObject = currentUser.getJSONObject("profile");
        try {
            if (jsonObject.has("facebookId")) {
                this.currentUserFbId = jsonObject.getString("facebookId");
            }
            if (jsonObject.has("name")){
                this.currentUserId = jsonObject.getString("name");
            }
        }catch (JSONException e){}
    }


}
