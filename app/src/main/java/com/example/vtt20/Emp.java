package com.example.vtt20;

public class Emp {
    private int Emp_ID,UserGroup_ID;
    private String Emp_NIC, Emp_Name, Emp_Mobile_Num, Sent_User, UserGroup_Name;

    public Emp(int Emp_ID, String Emp_NIC, String Emp_Name, String Emp_Mobile_Num, String Sent_User, String UserGroup_Name, int UserGroup_ID) {
        this.Emp_ID = Emp_ID;
        this.Emp_NIC = Emp_NIC;
        this.Emp_Name = Emp_Name;
        this.Emp_Mobile_Num = Emp_Mobile_Num;
        this.Sent_User = Sent_User;
        this.UserGroup_Name=UserGroup_Name;
        this.UserGroup_ID=UserGroup_ID;
    }

    public int get_Emp_ID() {
        return Emp_ID;
    }

    public String get_Emp_NIC() {
        return Emp_NIC;
    }

    public String get_Emp_Name() {
        return Emp_Name;
    }

    public String get_Emp_Mobile_Num() {
        return Emp_Mobile_Num;
    }

    public String get_Sent_User() {
        return Sent_User;
    }

    public String get_UserGroup_Name() {
        return UserGroup_Name;
    }

    public int get_UserGroup_ID() {
        return UserGroup_ID;
    }
}
