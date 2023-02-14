package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.*;
import android.widget.*;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class Notice_English extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024, CODE_POST_REQUEST = 1025;
    public static final int REQ_CODE_SPEECH_INPUT = 255;

    private Spinner NoticePriority,NoticeToWhom;
    private TextView mVoiceInputTv,NoticeLangType,NoticeCurrentDate,NoticeCurrentTime,NoticeUserLoginName,NoticeUserTypeLoginName,txtinvisibleUGID;
    private String ntPrt, ntContent, ntWhom, ntFrom, ntCurDate, ntCurTime, ntLangType, ntFromUType,db_u_group_num;
    int finalVarGroupNum;

    List<Notice> NoticeInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice__english);
        Button btnExitNotice = findViewById(R.id.btn_exit_eng);
        Button btnInsertNotice = findViewById(R.id.btn_send_eng);
        ImageButton btnInputVoice = findViewById(R.id.img_btn_eng);

        NoticePriority = findViewById(R.id.editPrt_eng);
        NoticeToWhom = findViewById(R.id.spin_to_whom_eng);

        loadSpinnerData(); //should fill by group types at the begin

        mVoiceInputTv = findViewById(R.id.editTextNotice_eng);
        mVoiceInputTv.setEnabled(false);

        NoticeUserLoginName = findViewById(R.id.label_user_login_name_eng);
        NoticeUserTypeLoginName = findViewById(R.id.label_user_type_login_name_eng);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        NoticeUserLoginName.setText(str);

        String str_2 = intent.getStringExtra("user_type");
        NoticeUserTypeLoginName.setText(str_2);

        NoticeLangType = findViewById(R.id.lang_english);
        NoticeCurrentDate = findViewById(R.id.label_current_date_eng);
        NoticeCurrentTime = findViewById(R.id.label_current_time_eng);
        txtinvisibleUGID = findViewById(R.id.invisibleUserGID);

        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        NoticeCurrentDate.setText(date_n);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        NoticeCurrentTime.setText(strDate);

        NoticeInfoList = new ArrayList<>();


        btnInputVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        btnInsertNotice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
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
                        formatToast.setText("Any field should not be Empty");
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
                            formatToast.setText("Not Entered");
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
                            formatToast.setText("Notice Entered Successfully");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            //and send to the mysql db
                            // after send to notice board
                            enterNoticeForDBServer();

                            //dangerous permission
                            ActivityCompat.requestPermissions(Notice_English.this,new String[]{Manifest.permission.SEND_SMS},1);
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
                new AlertDialog.Builder(Notice_English.this)
                        .setIcon(R.mipmap.main_3)
                        .setTitle("English Notice")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Notice_English.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",null)
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

            Notice_English.PerformNetworkRequest request = new Notice_English.PerformNetworkRequest(Api.URL_ENTER_NOTICE, params, CODE_POST_REQUEST);
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

    private void startVoiceInput() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            //Starts an activity that will prompt the user for speech and send it through a speech recognizer
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            //Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH
            //Use a language model based on free-form speech recognition
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            //Optional IETF language tag (as defined by BCP 47), for example - en-US
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter Your Notice");
            //Optional text prompt to show to the user when asking them to speak
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            //Starts an activity that will prompt the user for speech and send it through a speech recognizer
        } catch (Exception a) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //The results will be returned via activity results
        //if you start the intent or forwarded via a PendingIntent if one is provided
        try {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        mVoiceInputTv.setText(result.get(0));
                    }
                    break;
                }
            }
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