package com.example.habtrack;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserInfo {

    public final String userNameKey = "userName";
    public final String emailKey = "email";
    public final String passwordKey = "password";

    public String userName;
    public String email;
    public String password;

    public UserInfo() {

    }

    public UserInfo(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public HashMap<String, String> convertToKeyValuePair() {
        HashMap<String, String> userInfoData = new HashMap<>();

        userInfoData.put(userNameKey, this.userName);
        userInfoData.put(emailKey, this.email);
        userInfoData.put(passwordKey, this.password);

        return userInfoData;
    }

    public void convertFromKeyValuePair(HashMap<String, String> userInfoData) {
        this.userName = userInfoData.get(userNameKey);
        this.email = userInfoData.get(emailKey);
        this.password = userInfoData.get(passwordKey);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static DocumentReference getUserDatabaseReference(String userId) {
        return FirebaseFirestore.getInstance().collection("Users").document(userId);
    }
}
