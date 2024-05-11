package com.mr_deadrim.ebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageInfo;

import java.io.File;

public class AboutActivity extends AppCompatActivity {
    private View dialogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionTextView = findViewById(R.id.textView11);
        TextView appNameTextView = findViewById(R.id.textView13);
        TextView autherTextView = findViewById(R.id.textView15);


        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionTextView.setText(packageInfo.versionName);
            appNameTextView.setText(getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getPackageName(), 0)));
            autherTextView.setText(R.string.auther_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);



        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);


        item1.setEnabled(false);

        return true;
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
        Button cancelButton = dialogView.findViewById(R.id.button2);
        Button exitButton = dialogView.findViewById(R.id.button5);
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());
        exitButton.setOnClickListener(v12 -> {
            finishAffinity();
        });
        alertDialog.show();
    }




}