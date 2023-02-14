package com.example.vtt20;

public class Api {

    //if use localhost

    //SLT
    private static final String ROOT_URL = "http://192.168.1.100/voicetotextdb/v1/Api.php?apicall=";


    //if use hosted server
    //private static final String ROOT_URL = "http://voicetotext.pirilk.com/voicetotextdb/v1/Api.php?apicall=";

    public static final String URL_SIGN_UP = ROOT_URL + "user_signup";
    public static final String URL_GET_USERS = ROOT_URL + "get_users";
    public static final String URL_ENTER_NOTICE = ROOT_URL + "user_enternotice";
    public static final String URL_GROUP_CREATE = ROOT_URL + "user_groupcreate";
    public static final String URL_ASSIGN_EMPLOYEE = ROOT_URL + "emp_assign";
    public static final String URL_UPDATE_EMPLOYEE = ROOT_URL + "emp_update";
    public static final String URL_REMOVE_EMPLOYEE = ROOT_URL + "emp_remove&Emp_NIC=";
    // public static final String URL_SIGN_IN = ROOT_URL + "user_signin";
    //public static final String URL_GET_EMPLOYEE = ROOT_URL + "get_emp";
    public static final String URL_ADD_EMPLOYEE = ROOT_URL + "add_emp";
}

