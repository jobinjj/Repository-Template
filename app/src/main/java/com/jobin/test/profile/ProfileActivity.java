package com.jobin.test.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jobin.test.AppController;
import com.jobin.test.R;

public class ProfileActivity extends AppCompatActivity {
    String str_textView;
    ImageView img_edit;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iniViews();

        Bundle extras = getIntent().getExtras();
        String str_passedimage = extras.getString("imageurl");
        str_textView = extras.getString("sharedtext");
        NetworkImageView profile_image2 = (NetworkImageView) findViewById(R.id.profile_image2);
        TextView textView = (TextView) findViewById(R.id.textView);
        profile_image2.setImageUrl(str_passedimage,imageLoader);
        textView.setText(str_textView);
    }

    private void iniViews() {
        img_edit = (ImageView) findViewById(R.id.img_edit);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditActivity.class);
                intent.putExtra("username",str_textView);
                startActivity(intent);
            }
        });

    }
}
