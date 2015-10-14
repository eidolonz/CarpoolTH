package com.parse.carpool;

import com.parse.ParseObject;

import org.json.JSONObject;

/**
 * Created by JUMRUS on 21/9/2558.
 */
public class Trip{
    private String name;
    private String source;
    private String destination;
    private String phoneNo;
    private String email;
    private String car;
    private String description;
    private String objectId;
    private String ownerName;
    private long facebookId;
    private int money;
    private int passenger;
    private ParseObject createBy;
    private Object week;
    private Object day;
    private Object passengerId;

    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int passenger) {
        this.passenger = passenger;
    }

    public ParseObject getCreateBy() {
        return createBy;
    }

    public void setCreateBy(ParseObject createBy) {
        this.createBy = createBy;
    }

    public Object getWeek() {
        return week;
    }

    public void setWeek(Object week) {
        this.week = week;
    }

    public Object getDay() {
        return day;
    }

    public void setDay(Object day) {
        this.day = day;
    }

    public Object getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Object passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFacebookId() {
        return facebookId+"";
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }
}
