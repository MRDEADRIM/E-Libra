package com.mr_deadrim.elibra;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageInfo;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
public class AboutActivity extends AppCompatActivity {
    private View dialogView;
    TextView textViewAppVersion, textViewAppName, textViewAuthorName, textViewAboutDetail, textViewAbout, textViewAppNameLabel, textViewAppVersionLabel, textViewAuthorNameLabel,textViewExit,textViewExitMessage;
    JSONArray settingJsonArray;
    int textSize=40;
    Button buttonExitNo,buttonExitYes;
    String textStyle, orientationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        textViewAppVersion = findViewById(R.id.textViewAppVersion);
        textViewAppName = findViewById(R.id.textViewAppName);
        textViewAuthorName = findViewById(R.id.textViewAuthorName);
        textViewAboutDetail = findViewById(R.id.textViewAboutDetail);
        textViewAbout = findViewById(R.id.textViewAbout);
        textViewAppNameLabel = findViewById(R.id.textViewAppNameLabel);
        textViewAppVersionLabel = findViewById(R.id.textViewAppVersionLabel);
        textViewAuthorNameLabel = findViewById(R.id.textViewAuthorNameLabel);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            textViewAppVersion.setText(packageInfo.versionName);
            textViewAppName.setText(getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getPackageName(), 0)));
            textViewAuthorName.setText(R.string.auther_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        try{
            settingJsonArray = new JSONArray(prefs.getString("settingJsonArray", "[]"));
            JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
            textStyle = jsonObject0.getString("style");
            textSize = jsonObject0.getInt("size");
            JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
            orientationValue = jsonObject1.getString("value");
        }catch(Exception e){
            Toast.makeText(AboutActivity.this, "error in retreating json2array", Toast.LENGTH_SHORT).show();
        }
        load();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);
        setMenuItemStyle(item1, textStyle, textSize);
        setMenuItemStyle(item2, textStyle, textSize);
        setMenuItemStyle(item3, textStyle, textSize);
        item1.setEnabled(false);
        return true;
    }
    private void setMenuItemStyle(MenuItem item, String fontFamily, int textSize) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new TypefaceSpan(fontFamily), 0, spanString.length(), 0);
        spanString.setSpan(new TextAppearanceSpan(null, 0, textSize, null, null), 0, spanString.length(), 0);
        item.setTitle(spanString);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.item1){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId()==R.id.item2){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId()==R.id.item3){
            exit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void exit(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(AboutActivity.this);
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
            finishAffinity();
        });
        alertDialog.show();
    }
    public void load(){
        if(orientationValue.equals("Sensor")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        if(orientationValue.equals("Portrait")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(orientationValue.equals("Landscape")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewAppVersion.setTypeface(typeface);
        textViewAppName.setTypeface(typeface);
        textViewAuthorName.setTypeface(typeface);
        textViewAboutDetail.setTypeface(typeface);
        textViewAbout.setTypeface(typeface);
        textViewAppNameLabel.setTypeface(typeface);
        textViewAppVersionLabel.setTypeface(typeface);
        textViewAuthorNameLabel.setTypeface(typeface);
        textViewAppVersion.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAppName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAuthorName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAboutDetail.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAbout.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAppNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAppVersionLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewAuthorNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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

}