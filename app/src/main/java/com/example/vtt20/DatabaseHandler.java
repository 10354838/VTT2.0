package com.example.vtt20;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VoiceToTextDB";    // Database Name
    private static final int DATABASE_VERSION = 1;    // Database Version

    //------------------------------------------------------------------------------
    //Table UserGroup
    private static final String TABLE_NAME_1 = "UserGroup";   // Table Name
    private static final String UID_1="UserGroup_ID";     // Column I (Primary Key)
    private static final String NAME_1 = "UserGroup_Name";

    private static final String CREATE_TABLE_1 = "CREATE TABLE "+TABLE_NAME_1+
            " ("+UID_1+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "+NAME_1+" VARCHAR(50) NOT NULL);";
    private static final String DROP_TABLE_1 ="DROP TABLE IF EXISTS "+TABLE_NAME_1;

    //------------------------------------------------------------------------------
    //Table Employee
    private static final String TABLE_NAME_2 = "Employee";   // Table Name
    private static final String UID_2="Emp_ID";     // Column I (Primary Key)
    private static final String NAME_2 = "Emp_NIC"; // Employee NIC
    private static final String NAME_2_1 = "Emp_Name";
    private static final String NAME_2_2 = "Emp_Mobile_Num";
    private static final String NAME_2_4 = "Sent_User";
    private static final String NAME_2_5 = "UserGroup_Name";
    private static final String NAME_2_3 = "UserGroup_ID";

    private static final String CREATE_TABLE_2 = "CREATE TABLE "+TABLE_NAME_2+
            " ("+UID_2+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"+NAME_2+" VARCHAR(50) NOT NULL, "+NAME_2_1+" VARCHAR(50) NOT NULL, " +
            ""+NAME_2_2+" VARCHAR(20) NOT NULL,"+NAME_2_4+" VARCHAR(50) NOT NULL,"+NAME_2_5+" VARCHAR(50) NOT NULL,"+NAME_2_3+" INTEGER REFERENCES "+TABLE_NAME_1+");";
    private static final String DROP_TABLE_2 ="DROP TABLE IF EXISTS "+TABLE_NAME_2;

    //------------------------------------------------------------------------------
    //Table User
    private static final String TABLE_NAME_3 = "Users";   // Table Name
    private static final String UID_3="User_ID";     // Column I (Primary Key)
    private static final String NAME_3 = "User_NIC";
    private static final String NAME_3_1 = "User_Name";
    private static final String NAME_3_2 = "User_MobileNo";
    private static final String NAME_3_3 = "User_Pswd";
    private static final String NAME_3_3_2 = "UserGroup_Name";
    private static final String NAME_3_4 = "UserGroup_ID";

    private static final String CREATE_TABLE_3 = "CREATE TABLE "+TABLE_NAME_3+
            " ("+UID_3+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"+NAME_3+" VARCHAR(50) NOT NULL, "+NAME_3_1+" VARCHAR(50) NOT NULL, " +
            ""+NAME_3_2+" VARCHAR(20) NOT NULL,"+NAME_3_3+" VARCHAR(50) NOT NULL,"+NAME_3_3_2+" VARCHAR(50) NOT NULL,"+NAME_3_4+" INTEGER REFERENCES "+TABLE_NAME_1+");";
    private static final String DROP_TABLE_3 ="DROP TABLE IF EXISTS "+TABLE_NAME_3;

    //------------------------------------------------------------------------------
    //Table Notice
    private static final String TABLE_NAME_4 = "Notice";   // Table Name
    private static final String UID_4="Notice_ID";     // Column I (Primary Key)
    private static final String NAME_4_1_2 = "Notice_Priority";
    private static final String NAME_4_1_3 = "Notice_Description";
    private static final String NAME_4_1_4 = "Notice_ToWhom";
    private static final String NAME_4_1_4_2 = "Notice_From";
    private static final String NAME_4_1_4_3 = "Notice_From_U_Type";
    private static final String NAME_4_1_5= "Notice_SendDate";
    private static final String NAME_4_1_6= "Notice_SendTime";
    private static final String NAME_4_2= "Lang_Type";
    private static final String NAME_4_3= "UserGroup_ID";


    private static final String CREATE_TABLE_4 = "CREATE TABLE "+TABLE_NAME_4+
            " ("+UID_4+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "+NAME_4_1_2+" VARCHAR(20) NOT NULL, "+NAME_4_1_3+" VARCHAR(255) NOT NULL,"+NAME_4_1_4+" VARCHAR(50) NOT NULL,"+NAME_4_1_4_2+" VARCHAR(50) NOT NULL,"+NAME_4_1_4_3+" VARCHAR(50) NOT NULL," +
            ""+NAME_4_1_5+" VARCHAR(50) NOT NULL,"+NAME_4_1_6+" VARCHAR(50) NOT NULL,"
            +NAME_4_2+" VARCHAR(10) NOT NULL,"+NAME_4_3+" INTEGER REFERENCES "+TABLE_NAME_1+");";
    private static final String DROP_TABLE_4 ="DROP TABLE IF EXISTS "+TABLE_NAME_4;

    //------------------------------------------------------------------------------

    private Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Tables
        // Category table create query
        try {
            db.execSQL(CREATE_TABLE_1);
            db.execSQL(CREATE_TABLE_2);
            db.execSQL(CREATE_TABLE_3);
            db.execSQL(CREATE_TABLE_4);

            db.execSQL("INSERT INTO "+TABLE_NAME_1+" VALUES('1','Select Group')");
            db.execSQL("INSERT INTO "+TABLE_NAME_1+" VALUES('2','Administration')");
        } catch (Exception e) {
            Message.message(context,e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrading database
        // Drop older table if existed
        try {
            Message.message(context,"OnUpgrade");
            db.execSQL(DROP_TABLE_1);
            db.execSQL(DROP_TABLE_2);
            db.execSQL(DROP_TABLE_3);
            db.execSQL(DROP_TABLE_4);
            onCreate(db);
        }catch (Exception e) {
            Message.message(context,e.toString());
        }
    }

    public List<String> getAllLabels(){
        //used for loadSpinnerData() method in every file
        // Getting all labels returns list of labels
        List<String> list = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            String selectQuery = "SELECT "+NAME_1+" FROM " + TABLE_NAME_1;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if((cursor != null) && (cursor.getCount()>0)){
                cursor.moveToFirst();
                do {
                    list.add(cursor.getString(0));//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }catch (Exception e) {
            Message.message(context,e.toString());
        }
        return list;    // returning user type list;
    }//ok

    public List<String> GetGroupUserID(String user_group_name){
        // Load User employee id
        //used in Sign_in_screen.java
        List<String> list = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            String selectQuery = "SELECT * FROM "+TABLE_NAME_3+" WHERE "+NAME_3_3_2+"=?";
            Cursor cursor = db.rawQuery(selectQuery, new String[]{user_group_name});

            if((cursor != null) && (cursor.getCount()>0)){
                cursor.moveToFirst();
                do {
                    list.add(cursor.getString(1));//adding 2nd column data
                } while (cursor.moveToNext());
            }
        }catch (Exception e) {
            Message.message(context,e.toString());
        }
        return list;    // returning user type employee ID
    }//ok

    public ArrayList<HashMap<String, String>> GetUsers(){
        // Get User Details
        //used in DetailsActivity.java
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME_2;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("emp_id",cursor.getString(cursor.getColumnIndex(NAME_2)));
            user.put("emp_name",cursor.getString(cursor.getColumnIndex(NAME_2_1)));
            user.put("emp_mob_num",cursor.getString(cursor.getColumnIndex(NAME_2_2)));
            user.put("emp_assigned_type",cursor.getString(cursor.getColumnIndex(NAME_2_5)));
            userList.add(user);
        }
        return  userList;
    }//ok

    public ArrayList<HashMap<String, String>> GetGroups(){
        // Get groups
        //used in CreateUserGroups.java
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> groupList = new ArrayList<>();
        //String query = "SELECT * FROM "+ TABLE_NAME_1;
        String query = "SELECT * FROM "+ TABLE_NAME_1+" WHERE "+UID_1+"!=1";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> group = new HashMap<>();
            group.put("group_name",cursor.getString(cursor.getColumnIndex(NAME_1)));
            groupList.add(group);
        }
        return  groupList;
    }//ok

    public ArrayList<HashMap<String, String>> GetEmpList(){
        // Get employees
        //used in AddNewEmp.java
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> empList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME_2;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> emp = new HashMap<>();
            emp.put("emp_id",cursor.getString(cursor.getColumnIndex(NAME_2)));
            empList.add(emp);
        }
        return  empList;
    }//ok

    public ArrayList<HashMap<String, String>> GetGroupUsers(String emp_group_name){
        // Get User Details to remove and update
        //used in RemoveEmp.java - UpdateEmployee.java
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME_2+" WHERE "+NAME_2_5+"=?";
        Cursor cursor = db.rawQuery(query,new String[]{emp_group_name});
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("emp_id",cursor.getString(cursor.getColumnIndex(NAME_2)));
           // user.put("emp_mob_num",cursor.getString(cursor.getColumnIndex(NAME_2_2)));
            userList.add(user);
        }
        return  userList;
    }//ok

    public ArrayList<HashMap<String, String>> GetEmpMobNum(String ntWhom){
        // Get employee mobile numbers to send sms at once
        //used in Notice_English.java - Notice_Sinhala.java - Notice_Tamil.java
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME_2+" WHERE "+NAME_2_5+"=?";
        Cursor cursor = db.rawQuery(query,new String[]{ntWhom});
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("emp_mob_num",cursor.getString(cursor.getColumnIndex(NAME_2_2)));
            userList.add(user);
        }
        return  userList;
    }//ok

    public long UpdateEmpInfo(String emp_group, String emp_id, String emp_mob_num){
        //update assigned employee details
        //used in UpdateEmployee.java
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_2_2, emp_mob_num);
        return db.update(TABLE_NAME_2, values, NAME_2_5+" =? AND "+NAME_2+" =? " ,new String[]{emp_group,emp_id});
    }//ok

    public long MethodCreateUserGroup(String group_name){
        //To create new employee groups
        // used in CreateUserGroups.java
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_1, group_name);
        return db.insert(TABLE_NAME_1, null , values);
    }//ok

    public boolean MethodCheckExistUserGroup(String u_group) {
        //To check exist user group when creating new groups
        //used in CreateUserGroups.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_1+" WHERE "+NAME_1+"=?",new String[]{u_group});
        return (cursor.getCount()>0);
    }//ok

    public long MethodAssignUserGroups(int u_type_num, String u_type, String sent_user, String u_eid) {
        //To assigned employees to the groups
        // used in Assign_User_Role.java
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_2_3, u_type_num);
        values.put(NAME_2_5, u_type);
        values.put(NAME_2_4, sent_user);
        return db.update(TABLE_NAME_2, values, NAME_2+" =? ",new String[]{u_eid});
    }//ok

    public long MethodAddNewEmp(String u_eid, int tempUGID, String u_name, String u_mo_num, String sent_user, String u_type) {
        //To add new employees
        // used in AddNewEmp.java
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_2, u_eid);
        values.put(NAME_2_3, tempUGID);
        values.put(NAME_2_1, u_name);
        values.put(NAME_2_2, u_mo_num);
        values.put(NAME_2_4, sent_user);
        values.put(NAME_2_5, u_type);
        return db.insert(TABLE_NAME_2, null , values);
    }//ok

    public boolean MethodCheckExistEmpEmpID(String u_emp_id) {
        //To check exist NIC of employees
        //used in AddNewEmp.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_2+" WHERE "+NAME_2+"=?",new String[]{u_emp_id});
        return (cursor.getCount()>0);
    }//ok

    public boolean MethodCheckExistEmpMobileNo(String u_mobile_num) {
        //To check exist mobile number of employees
        //used in AddNewEmp.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_2+" WHERE "+NAME_2_2+"=?",new String[]{u_mobile_num});
        return (cursor.getCount()>0);
    }//ok

    public boolean MethodCheckExistEmpName(String u_name) {
        //To check exist user name of employees
        //used in AddNewEmp.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_2+" WHERE "+NAME_2_1+"=?",new String[]{u_name});
        return (cursor.getCount()>0);
    }//ok

    public long MethodSignUpUsers(int u_type, String u_eid, String u_name, String u_mo_num, String u_pswd, String u_group) {
        //To sign up users
        // used in Sign_up_screen.java
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_3_4,u_type);
        values.put(NAME_3,u_eid);
        values.put(NAME_3_1,u_name);
        values.put(NAME_3_2,u_mo_num);
        values.put(NAME_3_3,u_pswd);
        values.put(NAME_3_3_2,u_group);
        return db.insert(TABLE_NAME_3, null , values);
    }//ok

    public Cursor MethodSignInUsers(String u_group, String u_emp_id, String u_name, String u_pswd) {
        //To sign in users
        //used in Sign_in_screen.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_3+" WHERE "+NAME_3_3_2+"=? AND "+NAME_3+"=? AND "+ NAME_3_1+"=? AND "+NAME_3_3+"=?",new String[]{u_group,u_emp_id,u_name,u_pswd});
        if ((cursor != null) && (cursor.getCount()>0)) {
            cursor.moveToFirst();
        }
        return cursor;
    }//ok

    public Cursor MethodFindUserEmpID(String u_emp_id) {
        //To find user name and password for relevant employee id
        //used in Sign_in_screen.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_3+" WHERE "+NAME_3+"=?",new String[]{u_emp_id});
        if ((cursor != null) && (cursor.getCount()>0)) {
            cursor.moveToFirst();
        }
        return cursor;
    }//ok

    public Cursor MethodFindAssignEmpName(String u_emp_id) {
        //To find employee name
        //used in Assign_User_Role.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_2+" WHERE "+NAME_2+"=?",new String[]{u_emp_id});
        if ((cursor != null) && (cursor.getCount()>0)) {
            cursor.moveToFirst();
        }
        return cursor;
    }//ok

    public boolean MethodCaseSensitive(String u_group) {
        //To avoid entering the case sensitive groups that equal to already entered group
        //used in CreateUserGroups.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_1+" WHERE "+NAME_1+"=? COLLATE NOCASE",new String[]{u_group});
        return (cursor.getCount()>0);
    }//ok

    public boolean MethodCheckExistUserEmpID(String u_emp_id) {
        //To check exist emp id in sign up users
        //used in Sign_up_screen.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_3+" WHERE "+NAME_3+"=?",new String[]{u_emp_id});
        return (cursor.getCount()>0);
    }//ok

    public boolean MethodCheckExistUserMobileNo(String u_mobile_num) {
        //To check exist mobile num in sign up users
        //used in Sign_up_screen.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_3+" WHERE "+NAME_3_2+"=?",new String[]{u_mobile_num});
        return (cursor.getCount()>0);
    }//ok

    public long MethodInsertNotice(int utype_num, String nt_prt,String nt_content,String nt_to_whom,String nt_from,String nt_from_u_type,String nt_send_date,String nt_send_time,String nt_lang_type){
        //To insert the notice to the database
        // used in Notice_English.java - Notice_Sinhala.java - Notice_Tamil.java
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_4_3, utype_num);
        values.put(NAME_4_1_2, nt_prt);
        values.put(NAME_4_1_3, nt_content);
        values.put(NAME_4_1_4, nt_to_whom);
        values.put(NAME_4_1_4_2, nt_from);
        values.put(NAME_4_1_4_3, nt_from_u_type);
        values.put(NAME_4_1_5, nt_send_date);
        values.put(NAME_4_1_6, nt_send_time);
        values.put(NAME_4_2, nt_lang_type);
        return db.insert(TABLE_NAME_4, null , values);
    }//ok

    public long MethodRemoveEmp(String u_eid) {
        //To remove assigned users
        // used in RemoveEmp.java
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, NAME_2 + "=?", new String[]{u_eid});
    }//ok

    public Cursor MethodFindForgotPassword(String u_group,String u_emp_id,String u_name){
        //To find forgot password from the db
        //used in Sign_in_screen.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_3+" WHERE "+ NAME_3_3_2+"=? AND "+ NAME_3+"=? AND "+NAME_3_1+"=?",new String[]{u_group,u_emp_id,u_name});
        if ((cursor != null) && (cursor.getCount()>0)) {
            cursor.moveToFirst();
        }
        return cursor;
    }//ok

    public Cursor MethodFindUGID(String u_group){
        //To find user group id from the db
        //used in Assign_User_Role.java-Notice_English.java - Notice_Sinhala.java - Notice_Tamil.java
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_1+" WHERE "+ NAME_1+"=? ",new String[]{u_group});
        if ((cursor != null) && (cursor.getCount()>0)) {
            cursor.moveToFirst();
        }
        return cursor;
    }//ok
}
