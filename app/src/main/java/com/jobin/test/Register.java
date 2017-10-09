package com.jobin.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.google.android.gms.internal.zzapf.bit;
import static com.google.android.gms.internal.zznk.fj;
import static com.jobin.test.MainActivity.KEY_NAME;
import static com.jobin.test.MainActivity.KEY_TEXT;
import static com.jobin.test.R.id.editText;
import static com.jobin.test.R.id.editText2;
import static com.jobin.test.R.id.image;


public class Register extends AppCompatActivity{
    EditText username,password;
    ImageView imageView;
    Button register,upload;
    private Bitmap bitmap;
    public static final String KEY_NAME = "username";
    public static final String KEY_PASSWORD = "pass";
    public static final String KEY_IMAGE = "image";
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
        password = (EditText)findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
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
        final String pass = password.getText().toString().trim();
        final String image_url = getStringImage(bitmap);
        Toast.makeText(this, image_url, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(Register.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<>();
                param.put(KEY_PASSWORD,pass);
                param.put(KEY_NAME,name);
                param.put(KEY_IMAGE,image_url);

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

