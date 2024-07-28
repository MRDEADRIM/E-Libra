package com.mr_deadrim.elibra;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PermissionActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int APP_SETTINGS_REQUEST_CODE = 101;
    Button permission_btn;
    private View dialogView;

    TextView textView7;
    TextView textView5,textView;

    Button button2;
    Button button5;
    JSONArray json2Array;

    String style="sans-serif";

    int size=40;
    Button goToSettingCancelButton;
    Button goToSettingButton;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        textView=findViewById(R.id.textView);
        permission_btn = findViewById(R.id.button4);
        permission_btn.setOnClickListener(v -> {
            checkPermission();
        });

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {

            json2Array = new JSONArray(prefs.getString("key2", "[]"));
            JSONObject jsonObject0 = json2Array.getJSONObject(0);
            style=jsonObject0.getString("style");
            size=jsonObject0.getInt("size");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        main_text_change();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showSettingsDialog("new");
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    showSettingsDialog("old");
                }
            }
        }
    }

    private void showSettingsDialog(String status) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(PermissionActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.storage_permission_dialog, null);
        goToSettingCancelButton = dialogView.findViewById(R.id.go_to_setting_cancel_button);
        goToSettingButton = dialogView.findViewById(R.id.go_to_setting_button);
        textview = dialogView.findViewById(R.id.textView5);
        if(status.equals("new")){
            textview.setText(getString(R.string.storage_permission_new_text));
        }else{
            textview.setText(getString(R.string.storage_permission_old_text));
        }
        permission_text_change();
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

        textView7 = dialogView.findViewById(R.id.textView7);
        textView5 = dialogView.findViewById(R.id.textView5);
        button2 = dialogView.findViewById(R.id.button2);
        button5 = dialogView.findViewById(R.id.button5);
        exit_text_change();
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        button2.setOnClickListener(v1 -> alertDialog.dismiss());
        button5.setOnClickListener(v12 -> {
            finish();
        });
        alertDialog.show();
        return true;
    }
    public void permission_text_change(){
        Typeface typeface = Typeface.create(style, Typeface.NORMAL);
        goToSettingCancelButton.setTypeface(typeface);
        goToSettingButton.setTypeface(typeface);
        textview.setTypeface(typeface);

        goToSettingCancelButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        goToSettingButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);


    }
    public void exit_text_change(){
        Typeface typeface = Typeface.create(style, Typeface.NORMAL);
        textView7.setTypeface(typeface);
        textView5.setTypeface(typeface);
        button2.setTypeface(typeface);
        button5.setTypeface(typeface);

        textView7.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        button2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        button5.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }
    public void main_text_change(){

        Typeface typeface = Typeface.create(style, Typeface.NORMAL);
        textView.setTypeface(typeface);
        permission_btn.setTypeface(typeface);


        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        permission_btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

}