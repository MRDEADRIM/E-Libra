package com.mr_deadrim.ebook;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    public View dialogView;
    public EditText nameEditText,storageEditText;
    private static final int PICK_FILE_REQUEST_CODE = 1,PICK_IMAGE_REQUEST=21;
    RecyclerAdapter recyclerAdapter;
    private ImageView imageView;
    public String image_path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Button fileManager =dialogView.findViewById(R.id.button);
            Button imageButton =dialogView.findViewById(R.id.button3);
            imageView = dialogView.findViewById(R.id.imageButton);

            fileManager.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            });
            imageButton.setOnClickListener(view -> {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            });
            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());

            saveButton.setOnClickListener(v12 -> {
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

            });

            alertDialog.show();
        });

        recyclerAdapter = new RecyclerAdapter(jsonArray);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = getItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String filePath = FileUtils.getPathFromURI(this, uri);
                if (filePath != null) {
                    if (requestCode == 21) {
                        imageView.setImageURI(Uri.parse(filePath));
                        image_path=filePath;
                    }
                    if(requestCode == 22){
                        recyclerAdapter.setImagePath(filePath);
                    }
                    if (requestCode == 1){
                        storageEditText.setText(filePath);
                    }
                    if(requestCode == 2) {
                        recyclerAdapter.setFilePath(filePath);
                    }
                } else {
                    Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show();
                }
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