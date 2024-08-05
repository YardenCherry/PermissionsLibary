package com.example.permissionslibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.permissionslib.PermissionsManager;

public class MainActivity extends AppCompatActivity {

    private PermissionsManager permissionsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionsManager = new PermissionsManager(this);

        String[] permissions = {
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.INTERNET
        };

        permissionsManager.addPermissionsButton(permissions, 1);    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsManager != null) {
            permissionsManager.handlePermissionResult(requestCode, permissions, grantResults);
        }
    }
}

