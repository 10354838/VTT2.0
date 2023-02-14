package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        try{
            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<HashMap<String, String>> userList = db.GetUsers();
            ListView lv = findViewById(R.id.user_list);
            ListAdapter adapter = new SimpleAdapter(DetailsActivity.this, userList, R.layout.list_row,new String[]{"emp_assigned_type","emp_id","emp_name","emp_mob_num"}, new int[]{R.id.emp_type,R.id.emp_id, R.id.emp_name, R.id.emp_mob_num});
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
}