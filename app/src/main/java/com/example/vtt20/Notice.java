package com.example.vtt20;

public class Notice {
    private int Notice_ID,UserGroup_ID;
    private String Notice_Priority, Notice_Description, Notice_ToWhom, Notice_From, Notice_From_U_Type, Notice_SendDate,Notice_SendTime,Lang_Type;

    public Notice(int Notice_ID, String Notice_Priority, String Notice_Description, String Notice_ToWhom, String Notice_From, String Notice_From_U_Type, String Notice_SendDate,String Notice_SendTime,String Lang_Type,int UserGroup_ID) {
        this.Notice_ID = Notice_ID;
        this.Notice_Priority = Notice_Priority;
        this.Notice_Description = Notice_Description;
        this.Notice_ToWhom = Notice_ToWhom;
        this.Notice_From=Notice_From;
        this.Notice_From_U_Type=Notice_From_U_Type;
        this.Notice_SendDate=Notice_SendDate;
        this.Notice_SendTime=Notice_SendTime;
        this.Lang_Type=Lang_Type;
        this.UserGroup_ID=UserGroup_ID;
    }

    public int get_Notice_ID() {
        return Notice_ID;
    }

    public String get_Notice_Priority() {
        return Notice_Priority;
    }

    public String get_Notice_Description() {
        return Notice_Description;
    }

    public String get_Notice_ToWhom() {
        return Notice_ToWhom;
    }

    public String get_Notice_From() {
        return Notice_From;
    }

    public String get_Notice_From_U_Type() {
        return Notice_From_U_Type;
    }

    public String get_Notice_SendDate() {
        return Notice_SendDate;
    }

    public String get_Notice_SendTime() {
        return Notice_SendTime;
    }

    public String get_Lang_Type() {
        return Lang_Type;
    }

    public int get_UserGroup_ID() {
        return UserGroup_ID;
    }
}
