package com.example.vtt20;

public class User {
    private int User_ID,UserGroup_ID;
    private String User_NIC, User_Name, User_MobileNo, User_Pswd, UserGroup_Name;

    public User(int User_ID, String User_NIC, String User_Name, String User_MobileNo, String User_Pswd, String UserGroup_Name, int UserGroup_ID) {
        this.User_ID = User_ID;
        this.User_NIC = User_NIC;
        this.User_Name = User_Name;
        this.User_MobileNo = User_MobileNo;
        this.User_Pswd=User_Pswd;
        this.UserGroup_Name=UserGroup_Name;
        this.UserGroup_ID=UserGroup_ID;
    }

    public int get_User_ID() {
        return User_ID;
    }

    public String get_User_NIC() {
        return User_NIC;
    }

    public String get_User_Name() {
        return User_Name;
    }

    public String get_User_MobileNo() {
        return User_MobileNo;
    }

    public String get_User_Pswd() {
        return User_Pswd;
    }

    public String get_UserGroup_Name() {
        return UserGroup_Name;
    }

    public int get_UserGroup_ID() {
        return UserGroup_ID;
    }
}
