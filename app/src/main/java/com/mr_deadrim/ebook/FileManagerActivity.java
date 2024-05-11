package com.mr_deadrim.ebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FileManagerActivity extends AppCompatActivity {

    private ArrayList<String> fileList;
    private String internalStoragePath;
    private ListView listView;
    private TextView path_text;
    private String status;

    private static final String KEY_INTERNAL_STORAGE_PATH = null;

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
        path_text = findViewById(R.id.textView4);
        displayFiles(internalStoragePath);
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
            }
        }
        FileManagerAdapter adapter = new FileManagerAdapter(this, fileList);
        listView.setAdapter(adapter);
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
}


