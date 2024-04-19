package com.mr_deadrim.ebook;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class FileManagerActivity extends AppCompatActivity {

    private ArrayList<String> fileList;
    private String internalStoragePath;
    private ListView listView;
    public TextView path_text;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        internalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
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
    public void onBackPressed() {
        String parentPath = new File(internalStoragePath).getParent();
        if (parentPath != null && !parentPath.equals("/storage/emulated")) {
            internalStoragePath = parentPath;
            displayFiles(internalStoragePath);
        } else {
            super.onBackPressed();
        }
    }
}

