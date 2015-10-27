package com.parse.carpool;

/**
 * Created by JUMRUS on 25/10/2558.
 */
public class User {
    private String objectId;
    private String name;
    private String gender;
    private String email;
    private String mobileNo;
    private double rate;
    private int rater;
    private Object profile;
    private Object trip;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    public Object getTrip() {
        return trip;
    }

    public void setTrip(Object trip) {
        this.trip = trip;
    }
}
