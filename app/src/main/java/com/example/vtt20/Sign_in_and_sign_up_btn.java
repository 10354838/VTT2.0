package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Sign_in_and_sign_up_btn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_and_sign_up_btn);
        Button btnSignIn = findViewById(R.id.btn_sign_in);
        Button btnSignUp = findViewById(R.id.btn_sign_up);
        ImageView ImageView_2 = findViewById(R.id.imageView2);
        ImageView_2.setClipToOutline(true);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sign_in_and_sign_up_btn.this,Sign_in_screen.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sign_in_and_sign_up_btn.this,Sign_up_screen.class);
                startActivity(intent);
            }
        });
    }
}