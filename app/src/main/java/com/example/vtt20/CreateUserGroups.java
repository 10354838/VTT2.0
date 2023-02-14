package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.json.JSONObject;
import java.util.*;

public class CreateUserGroups extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024, CODE_POST_REQUEST = 1025;

    private EditText editTextGroupName;

    String VarGroupName;
    boolean blExistUserGroup,blCaseSensitiveGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_groups);
        Button createGroup = findViewById(R.id.btn_create_group);
        Button exitCreateGroup = findViewById(R.id.btn_create_group_exit);
        editTextGroupName = findViewById(R.id.editTextGroupName);
        final ListView lv = findViewById(R.id.listEmp);

        try{
            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<HashMap<String, String>> groupList = db.GetGroups();
            ListAdapter adapter = new SimpleAdapter(CreateUserGroups.this, groupList, R.layout.list_row_3,new String[]{"group_name"}, new int[]{R.id.group_type});
            lv.setAdapter(adapter);
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

        createGroup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    VarGroupName=editTextGroupName.getText().toString().trim();

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    blExistUserGroup=db.MethodCheckExistUserGroup(VarGroupName);
                    blCaseSensitiveGroup=db.MethodCaseSensitive(VarGroupName);

                    if(VarGroupName.isEmpty()){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("The field should not be Empty");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else if((blCaseSensitiveGroup) || (blExistUserGroup)){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Already Created User Group");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        editTextGroupName.setText("");
                    } else{
                        long id = db.MethodCreateUserGroup(VarGroupName);

                        if(id<=0)
                        {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Group Not Created");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                        else
                        {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Group Created");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                            ArrayList<HashMap<String, String>> groupList = db.GetGroups();
                            ListAdapter adapter = new SimpleAdapter(CreateUserGroups.this, groupList, R.layout.list_row_3,new String[]{"group_name"}, new int[]{R.id.group_type});
                            lv.setAdapter(adapter);

                            createUserGroupForDBServer();
                        }
                        editTextGroupName.setText("");
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

        exitCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CreateUserGroups.this)
                        .setIcon(R.mipmap.main_3)
                        .setTitle("Create User Groups")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(CreateUserGroups.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }
    private void createUserGroupForDBServer() {
        try{
            VarGroupName=editTextGroupName.getText().toString().trim();

            HashMap<String, String> params = new HashMap<>();
            params.put("UserGroup_Name", VarGroupName);

            CreateUserGroups.PerformNetworkRequest request = new CreateUserGroups.PerformNetworkRequest(Api.URL_GROUP_CREATE, params, CODE_POST_REQUEST);
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
}