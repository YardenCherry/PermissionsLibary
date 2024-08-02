package com.example.permissionslibrary;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.permissionslib.PermissionsListener;
import com.example.permissionslib.PermissionsManager;

public class MainActivity extends AppCompatActivity implements PermissionsListener {

    private static final int REQUEST_CODE = 1;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionsManager = new PermissionsManager(this, this);
        permissionsManager.requestPermissions(new String[]{
                android.Manifest.permission.READ_CONTACTS
                //android.Manifest.permission.CAMERA

        }, REQUEST_CODE);
    }

    @Override
    public void onPermissionGranted(String permission) {
        Toast.makeText(this, "Permission Granted: " + permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied(String permission) {
        Toast.makeText(this, "Permission Denied: " + permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionPermanentlyDenied(String permission) {
        Toast.makeText(this, "Permission Permanently Denied: " + permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.handlePermissionResult(requestCode, permissions, grantResults);
    }
}
