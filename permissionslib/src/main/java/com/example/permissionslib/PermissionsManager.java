package com.example.permissionslib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;

import java.util.LinkedList;
import java.util.Queue;

public class PermissionsManager implements PermissionsListener {
    private final Activity activity;
    private final Queue<PermissionRequest> permissionQueue = new LinkedList<>();
    private MaterialButton permissionsButton;
    private boolean isRequestingPermissions = false;

    public PermissionsManager(Activity activity) {
        this.activity = activity;
    }

    public void addPermissionsButton(String[] permissions, int requestCode) {
        FrameLayout rootLayout = activity.findViewById(android.R.id.content);

        permissionsButton = new MaterialButton(activity);
        permissionsButton.setId(View.generateViewId());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        permissionsButton.setLayoutParams(params);
        permissionsButton.setText("Request Permissions");
        permissionsButton.setContentDescription("requestPermissions");
        permissionsButton.setCornerRadius(24);
        permissionsButton.setOnClickListener(v -> requestPermissions(permissions, requestCode));

        rootLayout.addView(permissionsButton);
    }

    public void requestPermissions(String[] permissions, int requestCode) {
        for (String permission : permissions) {
            permissionQueue.add(new PermissionRequest(permission, requestCode));
        }
        if (!isRequestingPermissions) {
            processNextPermission();
        }
    }

    private void processNextPermission() {
        if (!permissionQueue.isEmpty()) {
            isRequestingPermissions = true;
            PermissionRequest nextRequest = permissionQueue.poll();
            if (nextRequest != null) {
                requestPermission(nextRequest.permission, nextRequest.requestCode);
            }
        } else {
            isRequestingPermissions = false;
        }
    }

    private void requestPermission(String permission, int requestCode) {
        if (PermissionsUtils.isPermissionGranted(activity, permission)) {
            onPermissionGranted(permission);
            processNextPermission();
        } else if (PermissionsUtils.shouldShowRequestPermissionRationale(activity, permission)) {
            showPermissionRationaleDialog(permission, requestCode);
        } else {
            PermissionsUtils.requestPermission(activity, new String[]{permission}, requestCode);
        }
    }

    private void showPermissionRationaleDialog(String permission, int requestCode) {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Required: " + getPermissionName(permission))
                .setMessage("This permission is required for the app to function properly. Please click 'Allow' to grant the permission.")
                .setPositiveButton("Allow", (dialog, which) -> PermissionsUtils.requestPermission(activity, new String[]{permission}, requestCode))
                .setNegativeButton("Deny", (dialog, which) -> {
                    dialog.dismiss();
                    onPermissionDenied(permission);
                })
                .show();
    }

    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permissions[i]);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                onPermissionDenied(permissions[i]);
                permissionQueue.add(new PermissionRequest(permissions[i], requestCode)); // Re-add denied permissions to the queue
            } else {
                onPermissionPermanentlyDenied(permissions[i]);
            }
        }
        processNextPermission();
    }

    @Override
    public void onPermissionGranted(String permission) {
        // Handle permission granted
    }

    @Override
    public void onPermissionDenied(String permission) {
        // Handle permission denied
    }

    @Override
    public void onPermissionPermanentlyDenied(String permission) {
        showPermanentlyDeniedDialog(permission);
    }

    private void showPermanentlyDeniedDialog(String permission) {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Required: " + getPermissionName(permission))
                .setMessage("The " + getPermissionName(permission) + " permission is required for this feature. Please enable it in the app settings.")
                .setPositiveButton("Open Settings", (dialog, which) -> openAppSettings())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    private String getPermissionName(String permission) {
        return permission.toLowerCase().substring(("android.permission.").length()).replace('_', ' ');
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
