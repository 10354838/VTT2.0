package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.json.JSONObject;
import java.util.*;

public class AddNEmp extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024, CODE_POST_REQUEST = 1025;

    private TextView nameCurrentUser,nameCurrentUserType;
    private EditText EmpID,EmpName,EmpMobileNum;
    private ListView lv;

    String strEmpID,strEmpName,strMobileNum,strCurrentUser,strCurrentUserType;
    int tempUGID=1;
    boolean blExistEmpID,blExistEmpMobileNum,blExistEmpName;

    final String NIC_PATTERN="^[0-9]{9}[vVxX]$";

    List<Emp> EmpInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_n_emp);
        nameCurrentUser = findViewById(R.id.txtCurrentUser);
        nameCurrentUserType = findViewById(R.id.txtCurrentUserType);
        EmpID = findViewById(R.id.txtEmpID);
        EmpName = findViewById(R.id.txtEmpName);
        EmpMobileNum = findViewById(R.id.txtEmpMobileNo);
        Button addEmp = findViewById(R.id.btn_add_emp);
        Button addEmpExit = findViewById(R.id.btn_add_emp_exit);
        lv = findViewById(R.id.listEmp);

        EmpInfoList = new ArrayList<>();

        Intent intent = getIntent();
        final String str = intent.getStringExtra("message");
        nameCurrentUser.setText(str);

        String str_2 = "Not Assigned";
        nameCurrentUserType.setText(str_2);

        try{
            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<HashMap<String, String>> empList = db.GetEmpList();
            ListAdapter adapter = new SimpleAdapter(AddNEmp.this, empList, R.layout.list_row_4,new String[]{"emp_id"}, new int[]{R.id.emp_id});
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

        addEmp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try{
                    strEmpID=EmpID.getText().toString().trim();
                    strEmpName=EmpName.getText().toString().trim();
                    strMobileNum=EmpMobileNum.getText().toString().trim();
                    strCurrentUser=nameCurrentUser.getText().toString().trim();
                    strCurrentUserType=nameCurrentUserType.getText().toString().trim();

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    blExistEmpID=db.MethodCheckExistEmpEmpID(strEmpID);
                    blExistEmpName=db.MethodCheckExistEmpName(strEmpName);
                    blExistEmpMobileNum=db.MethodCheckExistEmpMobileNo(strMobileNum);

                    if((strEmpID.isEmpty())||(strEmpName.isEmpty())||(strMobileNum.isEmpty())){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Any field should not be Empty");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else if(!(strEmpID.matches(NIC_PATTERN))){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Employee NIC is Invalid.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        EmpID.setText("");
                    } else if(strMobileNum.length()!=10){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Mobile Number is Invalid");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        EmpMobileNum.setText("");
                    } else if(blExistEmpID){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Already Entered Employee NIC");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        EmpID.setText("");
                    } else if(blExistEmpName){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Already Entered Employee Name");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        EmpName.setText("");
                    } else if(blExistEmpMobileNum){
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_warning, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView formatToast = layout.findViewById(R.id.txtToast);
                        formatToast.setText("Already Entered Mobile Number");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        EmpMobileNum.setText("");
                    } else{
                        long id = db.MethodAddNewEmp(strEmpID,tempUGID,strEmpName,strMobileNum,strCurrentUser,strCurrentUserType);
                        if(id<=0){
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_fail, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Not Entered");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        } else{
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView formatToast = layout.findViewById(R.id.txtToast);
                            formatToast.setText("Employee Added");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                            ArrayList<HashMap<String, String>> empList = db.GetEmpList();
                            ListAdapter adapter = new SimpleAdapter(AddNEmp.this, empList, R.layout.list_row_4,new String[]{"emp_id"}, new int[]{R.id.emp_id});
                            lv.setAdapter(adapter);

                            AddEmpForDBServer();
                        }
                        EmpID.setText("");
                        EmpName.setText("");
                        EmpMobileNum.setText("");
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

        addEmpExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new AlertDialog.Builder(AddNEmp.this)
                            .setIcon(R.mipmap.main_3)
                            .setTitle("Add New Employees")
                            .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(AddNEmp.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No",null)
                            .show();
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
    }
    private void AddEmpForDBServer() {
        try{
            strEmpID=EmpID.getText().toString().trim();
            strEmpName=EmpName.getText().toString().trim();
            strMobileNum=EmpMobileNum.getText().toString().trim();
            strCurrentUser=nameCurrentUser.getText().toString().trim();
            strCurrentUserType=nameCurrentUserType.getText().toString().trim();

            HashMap<String, String> params = new HashMap<>();
            params.put("Emp_NIC", strEmpID);
            params.put("UserGroup_ID", String.valueOf(tempUGID));
            params.put("Emp_Name", strEmpName);
            params.put("Emp_Mobile_Num", strMobileNum);
            params.put("Sent_User", strCurrentUser);
            params.put("UserGroup_Name", strCurrentUserType);

            AddNEmp.PerformNetworkRequest request = new AddNEmp.PerformNetworkRequest(Api.URL_ADD_EMPLOYEE, params, CODE_POST_REQUEST);
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