package com.mr_deadrim.ebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;


//public class MainActivity extends AppCompatActivity implements OnPageChangeListener {
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<String> list = new ArrayList<>();
    Button Add;
    int fromPosition, toPosition;
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
            for (int i = 0; i < jsonArray.length(); i++) {
                json = (JSONObject) jsonArray.get(i);
                name_list.add(json.getString("name"));
                storage_list.add(json.getString("storage"));
                list.add(name_list.get(i)+"\n"+storage_list.get(i));
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
                        Save(name.getText().toString(), storage.getText().toString());

                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        recyclerAdapter = new RecyclerAdapter(list);
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
                    for(int i = 0; i < list.size(); i++){
                        String[] text=list.get(i).split("\n");
                        json = new JSONObject();
                        json.put("name", text[0]);
                        json.put("storage", text[1]);
                        jsonArray.put(json);
                    }
                } catch (Exception e) {
                    Log.d("error", "Error spotted:"+e);
                }

                SharedPreferences prefs = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("key",  jsonArray.toString());
                editor.commit();
                Log.d("array_data", "save array data:"+jsonArray.toString());

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void Save(String name, String storage) {


        //adding data works fine
        name_list.add(name);
        storage_list.add(storage);
        list.add(name+"\n"+storage);
        jsonArray = new JSONArray();
        try {
            for(int i = 0; i < list.size(); i++){
                json = new JSONObject();
                json.put("name", name_list.get(i));
                json.put("storage", storage_list.get(i));
                jsonArray.put(json);
            }
        } catch (Exception e) {
            Log.d("error", "Error spotted:"+e);
        }
        SharedPreferences prefs = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key",  jsonArray.toString());
        editor.commit();
        Log.d("array_data", "save array data:"+jsonArray.toString());
    }



}