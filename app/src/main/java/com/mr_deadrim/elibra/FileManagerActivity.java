package com.mr_deadrim.elibra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class FileManagerActivity extends AppCompatActivity {

    private ArrayList<String> fileList;
    private String internalStoragePath;
    private ListView listView;
    private TextView path_text,textView21;
    private String status;

    private Button button11,button15;

    private static final String KEY_INTERNAL_STORAGE_PATH = null;

    JSONArray settingJsonArray;
    String selectedItem="sans-serif",orientation_value;

    int size=40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (savedInstanceState != null) {
            internalStoragePath = savedInstanceState.getString(KEY_INTERNAL_STORAGE_PATH);
        } else {
            internalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        fileList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        path_text = findViewById(R.id.textViewPath);

        button15 = findViewById(R.id.buttonFileManagerCancel);
        textView21 = findViewById(R.id.textViewFileManagerDivider);
        button11 = findViewById(R.id.buttonFileManagerSelect);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = fileList.get(position);
                File clickedFile = new File(fileName);
                if (clickedFile.isDirectory()) {
                    internalStoragePath = fileName;
                    displayFiles(internalStoragePath);
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("path", fileName);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        if (status.equals("select_folder")){
            button15.setVisibility(View.VISIBLE);
            textView21.setVisibility(View.VISIBLE);
            button11.setVisibility(View.VISIBLE);

            button15.setOnClickListener(view -> {
               finish();
            });
            button11.setOnClickListener(view -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("path", internalStoragePath);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }
        load();
        displayFiles(internalStoragePath);
        settings_text_change();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INTERNAL_STORAGE_PATH, internalStoragePath);
    }
    private void displayFiles(String directoryPath) {
        path_text.setText(directoryPath);
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                if (status.equals("add_document") && (file.isDirectory() || file.getName().toLowerCase().endsWith(".pdf"))) {
                    fileList.add(String.valueOf(file));
                }

                if (status.equals("add_image") && (file.isDirectory() || file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg"))) {
                    fileList.add(String.valueOf(file));
                }

                if(status.equals("select_folder") && file.isDirectory()){
                    fileList.add(String.valueOf(file));
                }

                if(status.equals("select_zip_file") && (file.isDirectory() || file.getName().toLowerCase().endsWith(".zip"))){
                    fileList.add(String.valueOf(file));
                }

            }
        }
        FileManagerAdapter adapter = new FileManagerAdapter(this, fileList,settingJsonArray);
        listView.setAdapter(adapter);
    }

    public void settings_text_change(){
        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        path_text.setTypeface(typeface);
        button11.setTypeface(typeface);
        button15.setTypeface(typeface);

        path_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        button11.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        button15.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String parentPath = new File(internalStoragePath).getParent();
        if (parentPath != null && !parentPath.equals("/storage/emulated")) {
            internalStoragePath = parentPath;
            displayFiles(internalStoragePath);
        } else {
            finish();
        }
        return true;
    }

    private void load() {

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            settingJsonArray = new JSONArray(prefs.getString("key2", "[]"));
            JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);

            selectedItem = jsonObject0.getString("style");
            size = jsonObject0.getInt("size");

            JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
            orientation_value = jsonObject1.getString("value");

            if(orientation_value.equals("Sensor")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            if(orientation_value.equals("Portrait")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if(orientation_value.equals("Landscape")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


