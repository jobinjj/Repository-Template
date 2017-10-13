package com.jobin.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class ProfileActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        String str_passedimage = extras.getString("imageurl");
        String str_textView = extras.getString("sharedtext");
        NetworkImageView profile_image2 = (NetworkImageView) findViewById(R.id.profile_image2);
        TextView textView = (TextView) findViewById(R.id.textView);
        profile_image2.setImageUrl(str_passedimage,imageLoader);
        textView.setText(str_textView);
    }
}
