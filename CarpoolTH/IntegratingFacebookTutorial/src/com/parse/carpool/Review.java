package com.parse.carpool;

        import com.parse.ParseClassName;
        import com.parse.ParseObject;

/**
 * Created by JUMRUS on 3/11/2558.
 */
@ParseClassName("Review")
public class Review extends ParseObject {
    public void setRating(double rating){
        put("rating", rating);
    }
    public float getRating(){
        return (float)getDouble("rating");
    }

    public void setUserId(String userId){
        put("userId", userId);
    }
    public String getUserId(){
        return getString("userId");
    }
    public void setTextReview(String text){
        put("textReview", text);
    }
    public String getTextReview(){
        return getString("textReview");
    }
    public void setRater(String rater){
        put("rater", rater);
    }
    public String getRater(){
        return getString("rater");
    }
    public void setUserFbId(String fbId){
        put("fbId", fbId);
    }
    public String getUserFbId(){
        return getString("fbId");
    }

    public void setTotalRating(double rating){
        put("totalRating", rating);
    }
    public double getTotalRating(){
        return getDouble("totalRating");
    }
    public void setRatingCounter(int counter){
        put("ratingCounter", counter);
    }
    public int getRatingCounter(){
        return getInt("ratingCounter");
    }
}
