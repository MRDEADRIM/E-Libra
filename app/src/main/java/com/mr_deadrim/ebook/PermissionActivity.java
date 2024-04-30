package com.mr_deadrim.ebook;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int APP_SETTINGS_REQUEST_CODE = 101;
    Button permission_btn;
    private View dialogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        permission_btn = findViewById(R.id.button4);
        permission_btn.setOnClickListener(v -> {
            checkPermission();
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showSettingsDialog();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void showSettingsDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(PermissionActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.storage_permission_dialog, null);
        Button goToSettingCancelButton = dialogView.findViewById(R.id.go_to_setting_cancel_button);
        Button goToSettingButton = dialogView.findViewById(R.id.go_to_setting_button);
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        goToSettingCancelButton.setOnClickListener(v1 -> alertDialog.dismiss());
        goToSettingButton.setOnClickListener(v12 -> {
            openAppSettings();
        });
        alertDialog.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        ((Activity) PermissionActivity.this).startActivityForResult(intent, APP_SETTINGS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_SETTINGS_REQUEST_CODE) {
            checkPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            checkPermission();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(PermissionActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        Button cancelButton = dialogView.findViewById(R.id.button2);
        Button exitButton = dialogView.findViewById(R.id.button5);
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());
        exitButton.setOnClickListener(v12 -> {
            finish();
        });
        alertDialog.show();
        return true;
    }

}