package com.mr_deadrim.ebook;

import android.Manifest;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<String> list = new ArrayList<>();
    Button Add;
    int fromPosition, toPosition, i;
    ArrayList<String> name_list = new ArrayList<>();
    ArrayList<String> storage_list = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Add = findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            jsonArray = new JSONArray(prefs.getString("key", "[]"));
            for (i = 0; i < jsonArray.length(); i++) {
                json = (JSONObject) jsonArray.get(i);
                name_list.add(json.getString("name"));
                storage_list.add(json.getString("storage"));
                list.add(name_list.get(i) + "\n" + storage_list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                View View = getLayoutInflater().inflate(R.layout.dialog, null);
                final EditText name = (EditText) View.findViewById(R.id.name);
                final EditText storage = (EditText) View.findViewById(R.id.storage);
                Button cancel = (Button) View.findViewById(R.id.btn_cancel);
                Button save = (Button) View.findViewById(R.id.btn_okay);
                alert.setView(View);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name_list.add(name.getText().toString());
                        storage_list.add(storage.getText().toString());
                        list.add(name.getText().toString() + "\n" + storage.getText().toString());

                        JSONObject json = new JSONObject();
                        try {
                            json.put("name", name.getText().toString());
                            json.put("storage", storage.getText().toString());
                            jsonArray.put(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, jsonArray.toString(), Toast.LENGTH_SHORT).show();
                        Save();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        recyclerAdapter = new RecyclerAdapter(jsonArray);
        recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();
                Collections.swap(list, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                Toast.makeText(MainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                jsonArray = new JSONArray();
                try {
                    for (i = 0; i < list.size(); i++) {
                        String[] text = list.get(i).split("\n");
                        json = new JSONObject();
                        json.put("name", text[0]);
                        json.put("storage", text[1]);
                        jsonArray.put(json);
                    }
                } catch (Exception e) {
                    Log.d("error", "Error spotted on move:" + e);
                    Toast.makeText(MainActivity.this, "Error spotted on save", Toast.LENGTH_SHORT).show();
                }
                Save();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void Save() {
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key", jsonArray.toString());
        editor.apply();
        Log.d("array_data", "save array data:" + jsonArray.toString());
    }
}