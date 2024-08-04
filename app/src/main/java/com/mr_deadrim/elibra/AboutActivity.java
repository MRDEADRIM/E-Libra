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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class AboutActivity extends AppCompatActivity {
    private View dialogView;
    TextView versionTextView,appNameTextView,autherTextView,textView12,textView16,textView9,textView10,textView14;
    JSONArray json2Array;
    String style;
    int size;
    TextView textView7;
    TextView textView5;

    Button button2;
    Button button5;
    String orientation_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        versionTextView = findViewById(R.id.textView11);
        appNameTextView = findViewById(R.id.textView13);
        autherTextView = findViewById(R.id.textView15);
        textView12 = findViewById(R.id.textView12);
        textView16=findViewById(R.id.textView16);
        textView9=findViewById(R.id.textView9);
        textView10=findViewById(R.id.textView10);
        textView14=findViewById(R.id.textView14);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionTextView.setText(packageInfo.versionName);
            appNameTextView.setText(getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getPackageName(), 0)));
            autherTextView.setText(R.string.auther_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        try{
            json2Array = new JSONArray(prefs.getString("key2", "[]"));
            JSONObject jsonObject0 = json2Array.getJSONObject(0);
            style = jsonObject0.getString("style");
            size = jsonObject0.getInt("size");
            JSONObject jsonObject1 = json2Array.getJSONObject(1);
            orientation_value = jsonObject1.getString("value");

        }catch(Exception e){
            Toast.makeText(AboutActivity.this, "error in retreating json2array", Toast.LENGTH_SHORT).show();
        }

        load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);



        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);

        setMenuItemStyle(item1, style, size);
        setMenuItemStyle(item2, style, size);
        setMenuItemStyle(item3, style, size);

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
            finishAffinity();
        });
        alertDialog.show();
    }
public void load(){

    if(orientation_value.equals("Sensor")){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    if(orientation_value.equals("Portrate")){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    if(orientation_value.equals("Landscape")){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }


    Typeface typeface = Typeface.create(style, Typeface.NORMAL);
    versionTextView.setTypeface(typeface);
    appNameTextView.setTypeface(typeface);
    autherTextView.setTypeface(typeface);
    textView12.setTypeface(typeface);
    textView16.setTypeface(typeface);
    textView9.setTypeface(typeface);
    textView10.setTypeface(typeface);
    textView14.setTypeface(typeface);


    versionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    appNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    autherTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    textView12.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    textView16.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    textView9.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    textView10.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    textView14.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

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


}