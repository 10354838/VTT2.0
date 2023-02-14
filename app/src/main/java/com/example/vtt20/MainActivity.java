package com.example.vtt20;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mInHome=new Intent(MainActivity.this,Sign_in_and_sign_up_btn.class);
                MainActivity.this.startActivity(mInHome);
                MainActivity.this.finish();
            }
        },3000);
    }
}