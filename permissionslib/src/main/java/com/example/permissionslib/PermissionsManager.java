package com.example.permissionslib;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class PermissionsManager {
    private final Activity activity;
    private final PermissionsListener listener;

    public PermissionsManager(Activity activity, PermissionsListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void requestPermission(String permission, int requestCode) {
        if (PermissionsUtils.isPermissionGranted(activity, permission)) {
            listener.onPermissionGranted(permission);
        } else if (PermissionsUtils.shouldShowRequestPermissionRationale(activity, permission)) {
            listener.onPermissionDenied(permission);
        } else {
            PermissionsUtils.requestPermission(activity, new String[]{permission}, requestCode);
        }
    }

    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                listener.onPermissionGranted(permissions[i]);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                listener.onPermissionDenied(permissions[i]);
            } else {
                listener.onPermissionPermanentlyDenied(permissions[i]);
            }
        }
    }
}
