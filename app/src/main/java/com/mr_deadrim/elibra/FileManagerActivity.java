package com.mr_deadrim.elibra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.Log;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileManagerActivity extends AppCompatActivity {
    private ArrayList<String> fileList;
    private String internalStoragePath;
    private ListView listView;
    private TextView textViewPath, textViewFileManagerDivider;
    private String status;
    private Button buttonFileManagerSelect, buttonFileManagerCancel;
    private static final String KEY_INTERNAL_STORAGE_PATH = null;
    JSONArray settingJsonArray;
    String selectedItem="sans-serif", orientationValue="Sensor";
    int textSize =40;
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
        textViewPath = findViewById(R.id.textViewPath);
        buttonFileManagerCancel = findViewById(R.id.buttonFileManagerCancel);
        textViewFileManagerDivider = findViewById(R.id.textViewFileManagerDivider);
        buttonFileManagerSelect = findViewById(R.id.buttonFileManagerSelect);

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
            buttonFileManagerCancel.setVisibility(View.VISIBLE);
            textViewFileManagerDivider.setVisibility(View.VISIBLE);
            buttonFileManagerSelect.setVisibility(View.VISIBLE);
            buttonFileManagerCancel.setOnClickListener(view -> {
               finish();
            });
            buttonFileManagerSelect.setOnClickListener(view -> {
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
        textViewPath.setText(directoryPath);
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                String fileType = getFileType(file);
                if (status.equals("add_document") && (file.isDirectory() || "pdf".equals(fileType))) {
                    fileList.add(file.getAbsolutePath());
                }
                if (status.equals("add_image") && (file.isDirectory() || "png".equals(fileType)  || "jpg".equals(fileType))) {
                    fileList.add(file.getAbsolutePath());
                }
                if (status.equals("select_folder") && file.isDirectory()) {
                    fileList.add(file.getAbsolutePath());
                }
                if (status.equals("select_zip_file") && (file.isDirectory() || "zip".equals(fileType) && file.getName().endsWith(".zip"))) {
                        fileList.add(file.getAbsolutePath());
                }
            }
        }
        FileManagerAdapter adapter = new FileManagerAdapter(this, fileList,settingJsonArray);
        listView.setAdapter(adapter);
    }

    public static String getFileType(File file) {
        if (file.isDirectory()) {
            return "directory";
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[16]; // Read the first 16 bytes of the file
            if (fis.read(header) != -1) {
                return getFileTypeFromHeader(header);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    private static String getFileTypeFromHeader(byte[] header) {
        // Check for PDF
        if (header.length >= 5 && header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46) {
            return "pdf";
        }
        // Check for PNG
        if (header.length >= 8 && header[0] == (byte) 0x89 && header[1] == (byte) 0x50 && header[2] == (byte) 0x4E && header[3] == (byte) 0x47) {
            return "png";
        }
        // Check for JPG
        if (header.length >= 2 && header[0] == (byte) 0xFF && header[1] == (byte) 0xD8) {
            return "jpg";
        }
        // Check for ZIP
        if (header.length >= 4 && header[0] == (byte) 0x50 && header[1] == (byte) 0x4B && header[2] == (byte) 0x03 && header[3] == (byte) 0x04) {
            return "zip";
        }
        // Add more file types here as needed
        return "unknown";
    }

    public void settings_text_change(){
        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        textViewPath.setTypeface(typeface);
        buttonFileManagerSelect.setTypeface(typeface);
        buttonFileManagerCancel.setTypeface(typeface);
        textViewPath.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonFileManagerSelect.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonFileManagerCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        try {
            settingJsonArray = new JSONArray(prefs.getString("settingJsonArray", "[]"));
            if (settingJsonArray.length() > 1) {
                JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
                selectedItem = jsonObject0.getString("style");
                textSize = jsonObject0.getInt("size");
                JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
                orientationValue = jsonObject1.getString("value");
            }
            if(orientationValue.equals("Sensor")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            if(orientationValue.equals("Portrait")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if(orientationValue.equals("Landscape")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


