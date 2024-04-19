package com.mr_deadrim.ebook;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    public View dialogView;
    public EditText nameEditText,storageEditText;
    private static final int PICK_FILE_REQUEST_CODE = 1, PICK_IMAGE_REQUEST_CODE =21;
    RecyclerAdapter recyclerAdapter;
    private ImageView imageView;
    public String image_path="";
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        Button addButton = findViewById(R.id.add);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            jsonArray = new JSONArray(prefs.getString("key", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addButton.setOnClickListener(v -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            dialogView = getLayoutInflater().inflate(R.layout.dialog, null);
            nameEditText = dialogView.findViewById(R.id.name);
            storageEditText = dialogView.findViewById(R.id.storage);
            Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
            Button saveButton = dialogView.findViewById(R.id.btn_okay);
            ImageView fileManager = dialogView.findViewById(R.id.button);
            ImageView imageButton = dialogView.findViewById(R.id.button3);
            imageView = dialogView.findViewById(R.id.imageButton);

            if(image_path.isEmpty()){
                imageView.setImageResource(R.mipmap.ic_launcher);
            }

            fileManager.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
                intent.putExtra("status", "add_document");
                ((Activity) MainActivity.this).startActivityForResult(intent, PICK_FILE_REQUEST_CODE);

            });
            imageButton.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
                intent.putExtra("status", "add_image");
                ((Activity) MainActivity.this).startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
            });
            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());

            saveButton.setOnClickListener(v12 -> {

                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    try {
                        JSONObject json = new JSONObject();
                        json.put("image_path",image_path);
                        json.put("name", nameEditText.getText().toString());
                        json.put("storage", storageEditText.getText().toString());
                        json.put("page",0);
                        json.put("total_pages",0);
                        jsonArray.put(json);
                        save();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerAdapter.notifyDataSetChanged();
                    image_path="";
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        });

        recyclerAdapter = new RecyclerAdapter(jsonArray);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = getItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private boolean CheckAllFields() {
        if (nameEditText.length() == 0) {
            nameEditText.setError("This field is required");
            return false;
        }

        if (storageEditText.length() == 0) {
            storageEditText.setError("This field is required");
            return false;
        }
        return true;
    }


    @NonNull
    private ItemTouchHelper getItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                try {
                    JSONObject temp = jsonArray.getJSONObject(fromPosition);
                    jsonArray.put(fromPosition, jsonArray.get(toPosition));
                    jsonArray.put(toPosition, temp);
                    recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                    save();
                } catch (JSONException e) {
                    Log.d("error", "Error spotted on move: " + e);
                    Toast.makeText(MainActivity.this, "Error spotted on move", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe actions if needed
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        return itemTouchHelper;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (data != null) {
                    String filePath = data.getStringExtra("path");
                    if (requestCode == 1){
                        storageEditText.setText(filePath);
                    }
                    if(requestCode == 2) {
                        recyclerAdapter.setFilePath(filePath);
                    }
                    if (requestCode == 21) {
                        imageView.setImageURI(Uri.parse(filePath));
                        image_path=filePath;
                    }
                    if(requestCode == 22){
                        recyclerAdapter.setImagePath(filePath);
                    }
                }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {

            } else {
                Intent intent = new Intent(this, PermissionActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ) {

            } else {
                Intent intent = new Intent(this, PermissionActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    private void save() {
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key", jsonArray.toString());
        editor.apply();
        Log.d("array_data", "save array data: " + jsonArray.toString());
    }
}