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

public class UpdateEmployee extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024, CODE_POST_REQUEST = 1025;

    private Spinner spn_user_group;
    private ListView lv;
    private TextView txt_Emp_ID;
    private EditText txtEmpMobNum;

    String VarGroupName,EmpMobNum,strNICtoString,replace1,replace2,finalValue;
    boolean blExistEmpMobileNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);
        Button btn_update = findViewById(R.id.update_btn);
        Button btn_Exit = findViewById(R.id.exit_emp_btn);
        Button btn_Load = findViewById(R.id.load_btn);

        lv = findViewById(R.id.listEmp);
        txt_Emp_ID = findViewById(R.id.txt_Up_Emp_Id);
        txtEmpMobNum = findViewById(R.id.txt_Up_Emp_Mob_Num);

        spn_user_group = findViewById(R.id.spin_user_grup_select);
        loadSpinnerData(); //should fill by group types at the begin

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

        btn_Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    VarGroupName=spn_user_group.getSelectedItem().toString().trim();

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    ArrayList<HashMap<String, String>> userList = db.GetGroupUsers(VarGroupName);
                    ListAdapter adapter = new SimpleAdapter(UpdateEmployee.this, userList, R.layout.list_row_4,new String[]{"emp_id"}, new int[]{R.id.emp_id});
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
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    VarGroupName=spn_user_group.getSelectedItem().toString().trim();
                    replace1=strNICtoString.replace("{emp_id=","");
                    replace2=replace1.replace("}","");
                    txt_Emp_ID.setText(replace2);
                    finalValue=txt_Emp_ID.getText().toString().trim();
                    EmpMobNum=txtEmpMobNum.getText().toString().trim();

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    blExistEmpMobileNum=db.MethodCheckExistEmpMobileNo(EmpMobNum);

                    if(EmpMobNum.isEmpty()){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Any field should not be Empty");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else if(EmpMobNum.length()!=10){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Enter Valid Mobile Number");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        txtEmpMobNum.setText("");
                    } else if(blExistEmpMobileNum){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Already Assigned Mobile Number");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        txtEmpMobNum.setText("");
                    } else{
                        long id = db.UpdateEmpInfo(VarGroupName,finalValue,EmpMobNum);
                        if(id<=0)
                        {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Not Updated");
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
                            formatToast.setText("Updated "+finalValue+" Mobile Number");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                            ArrayList<HashMap<String, String>> userList = db.GetGroupUsers(VarGroupName);
                            ListAdapter adapter = new SimpleAdapter(UpdateEmployee.this, userList, R.layout.list_row_4,new String[]{"emp_id"}, new int[]{R.id.emp_id});
                            lv.setAdapter(adapter);

                            UpdateEmpForDBServer();

                            Intent intent = new Intent(UpdateEmployee.this, DetailsActivity.class);
                            startActivity(intent);
                        }
                        txtEmpMobNum.setText("");
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

        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateEmployee.this)
                        .setIcon(R.mipmap.main_3)
                        .setTitle("Update Employees")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(UpdateEmployee.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }
    private void UpdateEmpForDBServer() {
        try{
            VarGroupName=spn_user_group.getSelectedItem().toString().trim();
            finalValue=txt_Emp_ID.getText().toString().trim();
            EmpMobNum=txtEmpMobNum.getText().toString().trim();

            HashMap<String, String> params = new HashMap<>();
            params.put("UserGroup_Name", VarGroupName);
            params.put("Emp_NIC", finalValue);
            params.put("Emp_Mobile_Num", EmpMobNum);

            UpdateEmployee.PerformNetworkRequest request = new UpdateEmployee.PerformNetworkRequest(Api.URL_UPDATE_EMPLOYEE, params, CODE_POST_REQUEST);
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
            spn_user_group.setAdapter(dataAdapter);
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