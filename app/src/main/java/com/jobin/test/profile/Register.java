package com.jobin.test.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobin.test.R;
import com.jobin.test.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class Register extends AppCompatActivity{
    EditText username,password,ed_email;
    ImageView imageView;
    Button register,upload;
    private Bitmap bitmap;
    public static final String KEY_NAME = "username";
    public static final String KEY_PASSWORD = "pass";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_EMAIL = "email";
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String UPLOAD_URL = "https://techpakka.com/android/user_registeration.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView back = (TextView) findViewById(R.id.back);
        imageView = (ImageView) findViewById(R.id.imageView);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        username = (EditText) findViewById(R.id.username);
        ed_email = (EditText) findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
                upload.setText("change");
            }
        });
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }


    public void uploadImage(){
        final String name = username.getText().toString().trim();
        final String email = ed_email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String image_url = getStringImage(bitmap);

        class UploadImage extends AsyncTask<Void,Void,String>{
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this,"Please wait","uploading details....",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                String str_response = s.trim();
                Toast.makeText(Register.this, s, Toast.LENGTH_SHORT).show();
                if (str_response.equals("Registered")){
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<>();
                param.put(KEY_PASSWORD,pass);
                param.put(KEY_NAME,name);
                param.put(KEY_IMAGE,image_url);
                param.put(KEY_EMAIL,email);

                return rh.sendPostRequest(UPLOAD_URL, param);
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }
    public void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filepath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}

