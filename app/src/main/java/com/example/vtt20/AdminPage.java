package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Button btnExit = findViewById(R.id.btn_exit);
        Button btnCreateGroup = findViewById(R.id.btn_create_grup_pg);
        Button btnUpdate = findViewById(R.id.btn_update_pg);
        Button btnRemove = findViewById(R.id.btn_remove_pg);
        Button btnNotice = findViewById(R.id.btn_notice_pg);
        Button btnAddEmp = findViewById(R.id.btn_add_emp_pg);
        Button btnAssignEmp = findViewById(R.id.btn_asign_emp_pg);

        TextView nameCurrentUser = findViewById(R.id.txtCurrentUserAdmin);
        TextView nameCurrentUserType = findViewById(R.id.txtCurrentUserType);
        Intent intent = getIntent();
        final String str = intent.getStringExtra("message");
        nameCurrentUser.setText(str);
        final String str_2 = intent.getStringExtra("user_type");
        nameCurrentUserType.setText(str_2);


        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPage.this,CreateUserGroups.class);
                startActivity(intent);
            }
        });

        btnAddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPage.this,AddNEmp.class);
                intent.putExtra("message",str);
                //intent.putExtra("user_type",str_2);
                startActivity(intent);
            }
        });

        btnAssignEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPage.this,Assign_User_Role.class);
                intent.putExtra("message",str);
                //intent.putExtra("user_type",str_2);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPage.this,UpdateEmployee.class);
                //intent.putExtra("message",str);
                //intent.putExtra("user_type",str_2);
                startActivity(intent);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPage.this,RemoveEmp.class);
                //intent.putExtra("message",str);
                //intent.putExtra("user_type",str_2);
                startActivity(intent);
            }
        });

        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPage.this,Lang_Selection.class);
                intent.putExtra("message",str);
                intent.putExtra("user_type",str_2);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdminPage.this)
                        .setIcon(R.mipmap.main_1)
                        .setTitle("Administration")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(AdminPage.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }
}