package com.jobin.test.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.jobin.test.AppController;
import com.jobin.test.HomeActivity;
import com.jobin.test.R;
import com.jobin.test.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.excludeName;
import static com.jobin.test.R.id.nav_name;
import static com.jobin.test.profile.Register.KEY_EMAIL;
import static com.jobin.test.profile.Register.KEY_IMAGE;

public class EditActivity extends AppCompatActivity {
    private static String UPLOAD_URL = "http://techpakka.com/android/user_details.php";
    private Bitmap bitmap;
    ImageView img_profile;
    String user_name;
    public static final String KEY_IMAGE = "image";
    Register register = new Register();
    private static int PICK_IMAGE_REQUEST = 1;
    Button btn_select,btn_update;
    Bundle extras;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        extras = getIntent().getExtras();
        user_name = extras.getString("username",null);
        initViews();
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_IMAGE_REQUEST);

    }

    private void initViews() {
        btn_select = (Button) findViewById(R.id.btn_select);
        btn_update = (Button) findViewById(R.id.btn_update);
        img_profile = (ImageView) findViewById(R.id.img_profile);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            Uri filepath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                img_profile.setImageBitmap(bitmap);
                Log.d("message",register.getStringImage(bitmap));

            } catch (IOException e) {
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
    public void uploadImage(){

        final String image_url = getStringImage(bitmap);

        class UploadImage extends AsyncTask<Void,Void,String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this,"Please wait","uploading details....",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Toast.makeText(EditActivity.this,s,Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<>();

                param.put(KEY_IMAGE,image_url);

                return rh.sendPostRequest(UPLOAD_URL, param);
            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

}
