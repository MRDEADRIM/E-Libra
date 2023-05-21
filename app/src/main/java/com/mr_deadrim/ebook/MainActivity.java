package com.mr_deadrim.ebook;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private Button addButton;
    private JSONArray jsonArray;
    public View dialogView;
    public EditText nameEditText,storageEditText;
    private static final int FILE_PICKER_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerView);

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

            fileManager.setOnClickListener(view -> {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                ((Activity) this).startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
            });

            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());

            saveButton.setOnClickListener(v12 -> {
                try {
                    JSONObject json = new JSONObject();
                    json.put("name", nameEditText.getText().toString());
                    json.put("storage", storageEditText.getText().toString());
                    jsonArray.put(json);
                    Toast.makeText(MainActivity.this, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            });

            alertDialog.show();
        });

        recyclerAdapter = new RecyclerAdapter(jsonArray);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            String filePath = selectedFileUri.getPath();

//            EditText storageEditText = dialogView.findViewById(R.id.storage);
            storageEditText.setText(filePath);

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