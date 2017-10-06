package com.jobin.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkConnectioCheck extends AppCompatActivity {
    String link = "https://www.techpakka.com";

HttpURLConnection conn;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_connectio_check);
        Button refresh = (Button) findViewById(R.id.refresh);

        try {
            url = new URL(link);
            URLConnection urlConn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkAvailable();
            }
        });
        isNetworkAvailable();

    }
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            Toast.makeText(this, "has network connection", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(this, "no network connection", Toast.LENGTH_SHORT).show();
        return false;
    }

}
