package com.jobin.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "pass";
    public static final String UPLOAD_URL = "http://techpakka.com/android/user_details.php";
    String name,str_username,str_password;
    String email;
    String image_url;
    Button btn_login;
    EditText edt_username,edt_password;
    SharedPreferences prefuserdetails;
    SharedPreferences.Editor editor;


    private SignInButton signInButton;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefuserdetails = getApplicationContext().getSharedPreferences("user_details", 0);
        editor = prefuserdetails.edit();
        setContentView(R.layout.activity_login);
        edt_username = (EditText) findViewById(R.id.username);
        edt_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.login);

        editor.putString("s","check");
        editor.apply();
        btn_login.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View view) {
                 getUserInput();
                 Login();
                  }
              });


        signInButton = (SignInButton) findViewById(R.id.signInButton);
        Button skip = (Button) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Register.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void signOut(){

    }
    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }
    private void handleResult(GoogleSignInResult googleSignInResult){
        if (googleSignInResult.isSuccess()){
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            name = account.getDisplayName();
            email = account.getEmail();
            image_url = account.getPhotoUrl().toString();

            updateUi(true);
        }
            else{
                updateUi(false);
            }





    }
    private void updateUi(boolean isLogin){
        if (isLogin){
            editor.putString("Name",name);
            editor.putString("gmail",email);
            editor.putString("image_url",image_url);
            editor.apply();

            String name = prefuserdetails.getString("Name", null);

            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
    public void getUserInput(){
        str_username = edt_username.getText().toString();
        str_password = edt_password.getText().toString();
    }
    public void Login(){

        class Login extends AsyncTask<Void,Void,String>{
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Please wait...","checking",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                loading.dismiss();

                startActivity(intent);
            }

            @Override
            protected String doInBackground(Void... Voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> param = new HashMap<>();
                param.put(KEY_PASSWORD,str_password);
                param.put(KEY_USERNAME,str_username);

                return rh.sendPostRequest(UPLOAD_URL,param);
            }
        }
        Login l = new Login();
        l.execute();
    }
}
