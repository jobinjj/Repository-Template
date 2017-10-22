package com.jobin.test.AdminPanel;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.jobin.test.AppController;
import com.jobin.test.Data;
import com.jobin.test.HomeActivity;
import com.jobin.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminEnquiry extends AppCompatActivity {
    private static String UPLOAD_URL = "http://techpakka.com/android/admin_query.php?";
    EditText ed_gmail,ed_message;
    Button send;
    String str_gmail,str_message;
    ConnectivityManager connectiviyManager;
    NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectiviyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = connectiviyManager.getActiveNetworkInfo();
        setContentView(R.layout.activity_admin_enquiry);
        intiViews();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_gmail = ed_gmail.getText().toString().trim();
                str_message  = ed_message.getText().toString().trim();
                sendData();
            }
        });


    }

    private void sendData() {
        final ProgressDialog  progress = ProgressDialog.show(AdminEnquiry.this,"Please wait...","checking",false,false);
        progress.setCanceledOnTouchOutside(true);
        if (netInfo == null){
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
        }
        else{
            JsonArrayRequest request = new JsonArrayRequest(UPLOAD_URL + "gmail=" + str_gmail + "&message=" + str_message,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progress.dismiss();
                            for (int i =0 ;i < response.length();i++){
                                try{
                                    JSONObject obj = response.getJSONObject(i);
                                    Data data = new Data();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(request);

        }
    }

    private void intiViews() {
        ed_gmail = (EditText) findViewById(R.id.ed_gmail);
        ed_message = (EditText) findViewById(R.id.ed_message);
        send = (Button) findViewById(R.id.send);


    }
}
