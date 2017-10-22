package com.jobin.test;

import android.*;
import android.Manifest;

import android.os.Bundle;
import android.widget.Toast;

public class Test extends AbsRuntimePermission {
    private static final int REQUEST_PERMISSION= 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        requestAppPermission(new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_CONTACTS },
                        R.string.msg,REQUEST_PERMISSION);

    }

    @Override
    public void onPermissionGranted(int requestCode) {
        Toast.makeText(this, "permission granted",Toast.LENGTH_SHORT).show();
    }

}
