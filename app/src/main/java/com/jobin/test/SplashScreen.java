package com.jobin.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jobin.test.profile.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i1 = new Intent(SplashScreen.this,HomeActivity.class);
                Intent i2 = new Intent(SplashScreen.this,LoginActivity.class);
                Boolean isLoggedin = pref.getBoolean("loggedin",false);
                if (isLoggedin){
                    finish();
                   startActivity(i1);
                }
                else {
                    finish();
                    startActivity(i2);
                }
            }
        }, 3000);
    }
}
