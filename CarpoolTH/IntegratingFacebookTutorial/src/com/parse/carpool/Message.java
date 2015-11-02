package com.parse.carpool;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by JUMRUS on 3/11/2558.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public String getUserId() {
        return getString("userId");
    }

    public String getBody() {
        return getString("body");
    }
    public String getRoomId(){
        return getString("roomId");
    }
    public String getFacebookId(){
        return getString("fbId");
    }
    public void setFacebookId(String fbId){
        put("fbId", fbId);
    }

    public void setRoomId(String roomId){
        put("roomId", roomId);
    }
    public void setUserId(String userId) {
        put("userId", userId);
    }

    public void setBody(String body) {
        put("body", body);
    }
}
