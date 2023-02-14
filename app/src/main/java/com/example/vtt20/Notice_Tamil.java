package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.*;
import android.widget.*;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class Notice_Tamil extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024, CODE_POST_REQUEST = 1025;
    // public static final int REQ_CODE_SPEECH_INPUT = 200;

    private Spinner NoticePriority,NoticeToWhom;
    private TextView mVoiceInputTv,NoticeLangType,NoticeCurrentDate,NoticeCurrentTime,NoticeUserLoginName,NoticeUserTypeLoginName,txtinvisibleUGID;
    String ntPrt, ntContent, ntWhom, ntFrom, ntCurDate, ntCurTime, ntLangType,ntFromUType,db_u_group_num;
    int finalVarGroupNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice__tamil);
        Button btnExitNotice = findViewById(R.id.btn_exit_tml);
        Button btnInsertNotice = findViewById(R.id.btn_send_tml);
        ImageButton btnInputVoice = findViewById(R.id.img_btn_tml);

        NoticePriority = findViewById(R.id.editPrt_tml);
        NoticeToWhom = findViewById(R.id.spin_to_whom_tml);

        loadSpinnerData(); //should fill by group types at the begin

        mVoiceInputTv = findViewById(R.id.editTextNotice_tml);
        //mVoiceInputTv.setEnabled(false);

        NoticeLangType = findViewById(R.id.lang_tamil);
        NoticeCurrentDate = findViewById(R.id.label_current_date_tml);
        NoticeCurrentTime = findViewById(R.id.label_current_time_tml);
        txtinvisibleUGID = findViewById(R.id.invisibleUserGID);

        NoticeUserLoginName = findViewById(R.id.label_user_login_name_tml);
        NoticeUserTypeLoginName = findViewById(R.id.label_user_type_login_name_tml);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        NoticeUserLoginName.setText(str);

        String str_2 = intent.getStringExtra("user_type");
        NoticeUserTypeLoginName.setText(str_2);

        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        NoticeCurrentDate.setText(date_n);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        NoticeCurrentTime.setText(strDate);

        btnInputVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startVoiceInput();
                //should input g board
            }
        });

        btnInsertNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ntPrt = NoticePriority.getSelectedItem().toString().trim();
                    ntContent = mVoiceInputTv.getText().toString().trim();
                    ntWhom = NoticeToWhom.getSelectedItem().toString().trim();
                    ntFrom=NoticeUserLoginName.getText().toString().trim();
                    ntFromUType=NoticeUserTypeLoginName.getText().toString().trim();
                    ntCurDate = NoticeCurrentDate.getText().toString().trim();
                    ntCurTime = NoticeCurrentTime.getText().toString().trim();
                    ntLangType = NoticeLangType.getText().toString().trim();

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor = db.MethodFindUGID(ntWhom);
                    do {
                        db_u_group_num = cursor.getString(0);
                    }while (cursor.moveToNext());
                    txtinvisibleUGID.setText(db_u_group_num);
                    finalVarGroupNum=Integer.parseInt(txtinvisibleUGID.getText().toString());


                    if((ntPrt.equals("Select Priority")) || (ntContent.isEmpty()) || (ntWhom.isEmpty()) || (ntFrom.isEmpty()) || (ntFromUType.isEmpty()) || (ntCurDate.isEmpty()) || (ntCurTime.isEmpty()) || (ntLangType.isEmpty())){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("எந்த துறையும் காலியாக இருக்கக்கூடாது.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else {
                        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        long id = db.MethodInsertNotice(finalVarGroupNum,ntPrt,ntContent,ntWhom,ntFrom,ntFromUType,ntCurDate,ntCurTime,ntLangType);
                        ArrayList<HashMap<String, String>> userList = db.GetEmpMobNum(ntWhom);

                        if(id<=0)
                        {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("அறிவிப்பு நுழையவில்லை.");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        } else
                        {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("அறிவிப்பு வெற்றிகரமாக நுழைந்தது.");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            //if this may be fail
                            //try for input keyboard method
                            enterNoticeForDBServer();

                            ActivityCompat.requestPermissions(Notice_Tamil.this,new String[]{Manifest.permission.SEND_SMS},1);
                            if(ntPrt.equals("High")){
                                for (int i = 0; i < userList.size(); i++)
                                {
                                    String message = mVoiceInputTv.getText().toString().trim();
                                    String tempMobileNumber = userList.get(i).toString();
                                    MultipleSMS(tempMobileNumber,message);
                                }
                            }//urgent messages send to mobile numbers
                        }
                        mVoiceInputTv.setText("");
                    }
                }catch (Exception e){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    TextView formatToast = layout.findViewById(R.id.txtToast);
                    formatToast.setText(e.toString());
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 100);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });


        btnExitNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Notice_Tamil.this)
                        .setIcon(R.mipmap.main_3)
                        .setTitle("தமிழ் அறிவிப்பு")
                        .setMessage("நிச்சயமாக நீங்கள் வெளியேற வேண்டுமா?")
                        .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Notice_Tamil.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("இல்லை",null)
                        .show();
            }
        });
    }
    private void enterNoticeForDBServer() {
        try{
            ntPrt = NoticePriority.getSelectedItem().toString().trim();
            ntContent = mVoiceInputTv.getText().toString().trim();
            ntWhom = NoticeToWhom.getSelectedItem().toString().trim();
            ntFrom=NoticeUserLoginName.getText().toString().trim();
            ntFromUType=NoticeUserTypeLoginName.getText().toString().trim();
            ntCurDate = NoticeCurrentDate.getText().toString().trim();
            ntCurTime = NoticeCurrentTime.getText().toString().trim();
            ntLangType = NoticeLangType.getText().toString().trim();


            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            Cursor cursor = db.MethodFindUGID(ntWhom);
            do {
                db_u_group_num = cursor.getString(0);
            }while (cursor.moveToNext());
            txtinvisibleUGID.setText(db_u_group_num);
            finalVarGroupNum=Integer.parseInt(txtinvisibleUGID.getText().toString());


            HashMap<String, String> params = new HashMap<>();
            params.put("UserGroup_ID", String.valueOf(finalVarGroupNum));
            params.put("Notice_Priority", ntPrt);
            params.put("Notice_Description", ntContent);
            params.put("Notice_ToWhom", ntWhom);
            params.put("Notice_From", ntFrom);
            params.put("Notice_From_U_Type", ntFromUType);
            params.put("Notice_SendDate", ntCurDate);
            params.put("Notice_SendTime", ntCurTime);
            params.put("Lang_Type", ntLangType);

            Notice_Tamil.PerformNetworkRequest request = new Notice_Tamil.PerformNetworkRequest(Api.URL_ENTER_NOTICE, params, CODE_POST_REQUEST);
            request.execute();
        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
            TextView formatToast = layout.findViewById(R.id.txtToast);
            formatToast.setText(e.toString());
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    /*LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    TextView formatToast = layout.findViewById(R.id.txtToast);
                    formatToast.setText(object.getString("message"));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 100);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();*/
                }
            } catch (Exception e) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                TextView formatToast = layout.findViewById(R.id.txtToast);
                formatToast.setText(e.toString());
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

//    private void startVoiceInput() {
//        try {
//            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "உங்கள் அறிவிப்பை உள்ளிடவும்");
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//        } catch (Exception a) {
//            Message.message(getApplicationContext(),a.toString());
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        try {
//            switch (requestCode) {
//                case REQ_CODE_SPEECH_INPUT: {
//                    if (resultCode == RESULT_OK && null != data) {
//                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                        mVoiceInputTv.setText(result.get(0));
//                    }
//                    break;
//                }
//
//            }
//        }catch (Exception a) {
//            Message.message(getApplicationContext(), a.toString());
//        }
//    }

    private void loadSpinnerData() {
        try{
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            List<String> labels = db.getAllLabels();

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            NoticeToWhom.setAdapter(dataAdapter);
        }catch (Exception a) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
            TextView formatToast = layout.findViewById(R.id.txtToast);
            formatToast.setText(a.toString());
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    private void MultipleSMS(String phoneNumber,String message) {
        try{
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

            // ---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            ContentValues values = new ContentValues();

                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            ArrayList<HashMap<String, String>> userList = db.GetEmpMobNum(ntWhom);

                            for (int i = 0; i < userList.size(); i++) {
                                values.put("number", userList.get(i).toString());
                                values.put("message", ntContent);
                            }

                            getContentResolver().insert(Uri.parse("content://sms/sent"), values);

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("SMS sent");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            LayoutInflater inflater2 = getLayoutInflater();
                            View layout2 = inflater2.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast2 = layout2.findViewById(R.id.txtToast);
                            formatToast2.setText("Generic failure");
                            Toast toast2 = new Toast(getApplicationContext());
                            toast2.setGravity(Gravity.BOTTOM, 0, 100);
                            toast2.setDuration(Toast.LENGTH_LONG);
                            toast2.setView(layout2);
                            toast2.show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            LayoutInflater inflater3 = getLayoutInflater();
                            View layout3 = inflater3.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast3 = layout3.findViewById(R.id.txtToast);
                            formatToast3.setText("No service");
                            Toast toast3 = new Toast(getApplicationContext());
                            toast3.setGravity(Gravity.BOTTOM, 0, 100);
                            toast3.setDuration(Toast.LENGTH_LONG);
                            toast3.setView(layout3);
                            toast3.show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            LayoutInflater inflater4 = getLayoutInflater();
                            View layout4 = inflater4.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast4 = layout4.findViewById(R.id.txtToast);
                            formatToast4.setText("Null PDU");
                            Toast toast4 = new Toast(getApplicationContext());
                            toast4.setGravity(Gravity.BOTTOM, 0, 100);
                            toast4.setDuration(Toast.LENGTH_LONG);
                            toast4.setView(layout4);
                            toast4.show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            LayoutInflater inflater5 = getLayoutInflater();
                            View layout5 = inflater5.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast5 = layout5.findViewById(R.id.txtToast);
                            formatToast5.setText("Radio off");
                            Toast toast5 = new Toast(getApplicationContext());
                            toast5.setGravity(Gravity.BOTTOM, 0, 100);
                            toast5.setDuration(Toast.LENGTH_LONG);
                            toast5.setView(layout5);
                            toast5.show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            // ---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
//                            LayoutInflater inflater = getLayoutInflater();
//                            View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
//                            TextView formatToast = layout.findViewById(R.id.txtToast);
//                            formatToast.setText("SMS delivered");
//                            Toast toast = new Toast(getApplicationContext());
//                            toast.setGravity(Gravity.BOTTOM, 0, 100);
//                            toast.setDuration(Toast.LENGTH_LONG);
//                            toast.setView(layout);
//                            toast.show();
                            break;
                        case Activity.RESULT_CANCELED:
                            LayoutInflater inflater2 = getLayoutInflater();
                            View layout2 = inflater2.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast2 = layout2.findViewById(R.id.txtToast);
                            formatToast2.setText("SMS not delivered");
                            Toast toast2 = new Toast(getApplicationContext());
                            toast2.setGravity(Gravity.BOTTOM, 0, 100);
                            toast2.setDuration(Toast.LENGTH_LONG);
                            toast2.setView(layout2);
                            toast2.show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }catch (Exception e){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
            TextView formatToast = layout.findViewById(R.id.txtToast);
            formatToast.setText(e.toString());
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }
}