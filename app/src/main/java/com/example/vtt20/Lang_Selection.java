package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Lang_Selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang__selection);
        Button langSinhala = findViewById(R.id.btn_sinhala);
        Button langTamil = findViewById(R.id.btn_tamil);
        Button langEnglish = findViewById(R.id.btn_english);

        TextView nameCurrentUser = findViewById(R.id.txtCurrentUser);
        final TextView nameCurrentUserType = findViewById(R.id.label_user_type_login_name_eng);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("message");
        nameCurrentUser.setText(str);

        final String str_2 = intent.getStringExtra("user_type");
        nameCurrentUserType.setText(str_2);

        langSinhala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Lang_Selection.this,Notice_Sinhala.class);
                intent.putExtra("message", str);
                intent.putExtra("user_type", str_2);
                startActivity(intent);
            }
        });

        langTamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Lang_Selection.this,Notice_Tamil.class);
                intent.putExtra("message", str);
                intent.putExtra("user_type", str_2);
                startActivity(intent);
            }
        });

        langEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Lang_Selection.this,Notice_English.class);
                intent.putExtra("message", str);
                intent.putExtra("user_type", str_2);
                startActivity(intent);
            }
        });
    }
}