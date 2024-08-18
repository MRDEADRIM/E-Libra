package com.mr_deadrim.elibra;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

    private View dialogView;
    TextView textViewExit;
    TextView textViewExitMessage,textViewPermissionDescription,textViewAuthorization,textViewAuthorizationMessage;
    JSONArray settingJsonArray;
    String textStyle="sans-serif", orientationValue ="Sensor";
    int textSize=40;
    Button buttonExitNo,buttonExitYes,buttonGrandPermission,buttonGoToSettingCancel, buttonGoToSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        textViewPermissionDescription =findViewById(R.id.textViewPermissionDescription);
        buttonGrandPermission = findViewById(R.id.buttonGrandPermission);
        buttonGrandPermission.setOnClickListener(v -> {
            checkPermission();
        });
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        try {
            settingJsonArray = new JSONArray(prefs.getString("settingJsonArray", "[]"));
            JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
            textStyle =jsonObject0.getString("style");
            textSize =jsonObject0.getInt("size");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        load();

    }
    private void load() {
        try {
            JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
            textStyle=jsonObject0.getString("style");
            textSize =jsonObject0.getInt("size");
            JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
            orientationValue = jsonObject1.getString("value");
            if(orientationValue.equals("Sensor")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            if(orientationValue.equals("Portrait")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if(orientationValue.equals("Landscape")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
            main_text_change();
        }catch (Exception e){
            e.printStackTrace();
        }

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
        buttonGoToSettingCancel = dialogView.findViewById(R.id.buttonGoToSettingCancel);
        buttonGoToSetting = dialogView.findViewById(R.id.buttonGoToSetting);
        textViewAuthorizationMessage = dialogView.findViewById(R.id.textViewAuthorizationMessage);
        textViewAuthorization = dialogView.findViewById(R.id.textViewAuthorization);
        if(status.equals("new")){
            textViewAuthorizationMessage.setText(getString(R.string.storage_permission_new_text));
        }else{
            textViewAuthorizationMessage.setText(getString(R.string.storage_permission_old_text));
        }
        permission_text_change();
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        buttonGoToSettingCancel.setOnClickListener(v1 -> alertDialog.dismiss());
        buttonGoToSetting.setOnClickListener(v12 -> {
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
        textViewExit = dialogView.findViewById(R.id.textViewExit);
        textViewExitMessage = dialogView.findViewById(R.id.textViewExitMessage);
        buttonExitNo = dialogView.findViewById(R.id.buttonExitNo);
        buttonExitYes = dialogView.findViewById(R.id.buttonExitYes);
        exit_text_change();
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        buttonExitNo.setOnClickListener(v1 -> alertDialog.dismiss());
        buttonExitYes.setOnClickListener(v12 -> {
            finish();
        });
        alertDialog.show();
        return true;
    }
    public void permission_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        buttonGoToSettingCancel.setTypeface(typeface);
        buttonGoToSetting.setTypeface(typeface);
        textViewAuthorizationMessage.setTypeface(typeface);
        textViewAuthorization.setTypeface(typeface);
        buttonGoToSettingCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonGoToSetting.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAuthorizationMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAuthorization.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    public void exit_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewExit.setTypeface(typeface);
        textViewExitMessage.setTypeface(typeface);
        buttonExitNo.setTypeface(typeface);
        buttonExitYes.setTypeface(typeface);
        textViewExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewExitMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonExitNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonExitYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    public void main_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewPermissionDescription.setTypeface(typeface);
        buttonGrandPermission.setTypeface(typeface);
        textViewPermissionDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonGrandPermission.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}