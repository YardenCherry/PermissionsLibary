package com.example.permissionslib;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import java.util.LinkedList;
import java.util.Queue;

public class PermissionsManager {
    private final Activity activity;
    private final PermissionsListener listener;
    private final Queue<PermissionRequest> permissionQueue = new LinkedList<>();

    public PermissionsManager(Activity activity, PermissionsListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void requestPermissions(String[] permissions, int requestCode) {
        for (String permission : permissions) {
            permissionQueue.add(new PermissionRequest(permission, requestCode));
        }
        processNextPermission();
    }

    private void processNextPermission() {
        PermissionRequest nextRequest = permissionQueue.poll();
        if (nextRequest != null) {
            requestPermission(nextRequest.permission, nextRequest.requestCode);
        }
    }

    private void requestPermission(String permission, int requestCode) {
        if (PermissionsUtils.isPermissionGranted(activity, permission)) {
            listener.onPermissionGranted(permission);
            processNextPermission();
        } else if (PermissionsUtils.shouldShowRequestPermissionRationale(activity, permission)) {
            listener.onPermissionDenied(permission);
            // Request the permission again
            PermissionsUtils.requestPermission(activity, new String[]{permission}, requestCode);
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
                // Request the permission again
                PermissionsUtils.requestPermission(activity, new String[]{permissions[i]}, requestCode);
            } else {
                listener.onPermissionPermanentlyDenied(permissions[i]);
            }
        }
        processNextPermission();
    }

    private static class PermissionRequest {
        String permission;
        int requestCode;

        PermissionRequest(String permission, int requestCode) {
            this.permission = permission;
            this.requestCode = requestCode;
        }
    }
}
