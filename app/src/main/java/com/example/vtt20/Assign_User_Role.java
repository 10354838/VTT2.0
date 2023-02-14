package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.json.JSONObject;
import java.util.*;

public class Assign_User_Role extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024, CODE_POST_REQUEST = 1025;

    private Spinner selectUserGroup;
    private TextView nameCurrentUser,listSelectedValue, txtinvisibleNIC, txtinvisibleUGID, txtinvisibleUGName, txtinvisibleCurUser;
    private ListView lv;

    String db_u_name,CurrentUser,VarGroupName,strNICtoString,finalValue,replace1,replace2, svrVarGroupName, svrfinalValue, svrCurrentUser, db_u_group_num;
    int finalVarGroupNum;
    List<Emp> EmpInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign__user__role);
        Button assignExit = findViewById(R.id.btn_assign_exit);
        Button assignGroup = findViewById(R.id.btn_assign_group);
        txtinvisibleUGID = findViewById(R.id.invisibleUserGID);
        txtinvisibleUGName = findViewById(R.id.invisibleUserGName);
        txtinvisibleCurUser = findViewById(R.id.invisibleCurUser);
        txtinvisibleNIC = findViewById(R.id.invisibleNIC);
        lv = findViewById(R.id.listEmp);

        EmpInfoList = new ArrayList<>();

        selectUserGroup = findViewById(R.id.spin_user_grup_select);
        loadSpinnerData(); //should fill by group types at the begin

        listSelectedValue = findViewById(R.id.lstSelectText);
        nameCurrentUser = findViewById(R.id.txtUserNameAssign);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        nameCurrentUser.setText(str);

        try{
            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<HashMap<String, String>> empList = db.GetEmpList();
            ListAdapter adapter = new SimpleAdapter(Assign_User_Role.this, empList, R.layout.list_row_4,new String[]{"emp_id"}, new int[]{R.id.emp_id});
            lv.setAdapter(adapter);
        } catch (Exception e){
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

        //callback method when invoke to selected items in view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    strNICtoString=lv.getItemAtPosition(position).toString();
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

        assignGroup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                    VarGroupName=selectUserGroup.getSelectedItem().toString().trim();
                    Cursor cursor = db.MethodFindUGID(VarGroupName);
                    do {
                        db_u_group_num = cursor.getString(0);
                    }while (cursor.moveToNext());
                    txtinvisibleUGID.setText(db_u_group_num);
                    finalVarGroupNum=Integer.parseInt(txtinvisibleUGID.getText().toString());

                    replace1=strNICtoString.replace("{emp_id=","");
                    replace2=replace1.replace("}","");
                    listSelectedValue.setText(replace2);
                    finalValue=listSelectedValue.getText().toString().trim();

                    CurrentUser=nameCurrentUser.getText().toString().trim();

                    if(VarGroupName.equals("Select Group")){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Select Group Name");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else{
                        long id=db.MethodAssignUserGroups(finalVarGroupNum,VarGroupName,CurrentUser,finalValue);

                        if(id<=0)
                        {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Not Assigned");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        } else
                        {
                            Cursor cursor2 = db.MethodFindAssignEmpName(finalValue);

                            do {
                                db_u_name=cursor2.getString(2);
                            }while (cursor2.moveToNext());

                            LayoutInflater inflater2 = getLayoutInflater();
                            View layout2 = inflater2.inflate(R.layout.toast_info, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast2 = layout2.findViewById(R.id.txtToast);
                            formatToast2.setText("Employee is "+db_u_name);
                            Toast toast2 = new Toast(getApplicationContext());
                            toast2.setGravity(Gravity.CENTER_HORIZONTAL, 0, 100);
                            toast2.setDuration(Toast.LENGTH_SHORT);
                            toast2.setView(layout2);
                            toast2.show();

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Employee Assigned to "+VarGroupName+ " Group");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                            assignUserGroupForDBServer();

                            Intent intent = new Intent(Assign_User_Role.this, DetailsActivity.class);
                            startActivity(intent);
                        }
                        selectUserGroup.setSelection(0);
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

        assignExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Assign_User_Role.this)
                        .setIcon(R.mipmap.main_3)
                        .setTitle("Assign Employees")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Assign_User_Role.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }
    private void assignUserGroupForDBServer() {
        try{
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            VarGroupName=selectUserGroup.getSelectedItem().toString().trim();
            Cursor cursor = db.MethodFindUGID(VarGroupName);
            do {
                db_u_group_num = cursor.getString(0);
            }while (cursor.moveToNext());
            txtinvisibleUGID.setText(db_u_group_num);
            finalVarGroupNum=Integer.parseInt(txtinvisibleUGID.getText().toString());

            txtinvisibleUGName.setText(selectUserGroup.getSelectedItem().toString().trim());
            txtinvisibleCurUser.setText(nameCurrentUser.getText().toString().trim());
            txtinvisibleNIC.setText(listSelectedValue.getText().toString().trim());

            svrVarGroupName=txtinvisibleUGName.getText().toString().trim();
            svrfinalValue=txtinvisibleNIC.getText().toString().trim();
            svrCurrentUser=txtinvisibleCurUser.getText().toString().trim();

            HashMap<String, String> params = new HashMap<>();
            params.put("UserGroup_ID", String.valueOf(finalVarGroupNum));
            params.put("UserGroup_Name", svrVarGroupName);
            params.put("Sent_User", svrCurrentUser);
            params.put("Emp_NIC", svrfinalValue);

            Assign_User_Role.PerformNetworkRequest request = new Assign_User_Role.PerformNetworkRequest(Api.URL_ASSIGN_EMPLOYEE, params, CODE_POST_REQUEST);
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

    private void loadSpinnerData() {
        try{
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            List<String> labels = db.getAllLabels();

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            selectUserGroup.setAdapter(dataAdapter);
        }catch (Exception e) {
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