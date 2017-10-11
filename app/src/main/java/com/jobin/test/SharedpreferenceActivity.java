package com.jobin.test;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SharedpreferenceActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedpreference);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("hello","hai");
        editor.apply();

        Button btn_chkdpref = (Button) findViewById(R.id.btn_chckpref);
        btn_chkdpref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_pref = pref.getString("hello",null);
                Toast.makeText(SharedpreferenceActivity.this, str_pref, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
