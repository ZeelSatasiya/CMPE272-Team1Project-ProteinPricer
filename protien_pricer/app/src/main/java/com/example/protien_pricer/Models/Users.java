package com.example.protien_pricer.Models;

public class Users {
    String UserId,UserName,ProfilePic;

    public Users(String userId, String userName, String profilePic) {
        UserId = userId;
        UserName = userName;
        ProfilePic = profilePic;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
    public Users(){}
}
