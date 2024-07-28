package com.mr_deadrim.elibra;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    private View dialogView;
    public EditText nameEditText,storageEditText;
    private static final int PICK_FILE_REQUEST_CODE = 1, PICK_IMAGE_REQUEST_CODE =21;
    RecyclerAdapter recyclerAdapter;
    private ImageView imageView;
    public String image_path="";
    boolean isAllFieldsChecked = false;
    JSONArray json2Array;
    String orientation_value="Sensor";

    String selectedItem="sans-serif";

    int size=40;

    Button cancelButton;
    Button saveButton;
    ImageView fileManager,imageButton;
    TextView textView3,textView2;
    Button addButton;

    TextView textView7;
    TextView textView5;

    Button button2;
    Button button5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        addButton = findViewById(R.id.add);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            jsonArray = new JSONArray(prefs.getString("key", "[]"));
            json2Array = new JSONArray(prefs.getString("key2", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        load();


        addButton.setOnClickListener(v -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            dialogView = getLayoutInflater().inflate(R.layout.dialog, null);
            textView3 = dialogView.findViewById(R.id.textView3);
            textView2 = dialogView.findViewById(R.id.textView2);
            nameEditText = dialogView.findViewById(R.id.name);
            storageEditText = dialogView.findViewById(R.id.storage);
            cancelButton = dialogView.findViewById(R.id.btn_cancel);
            saveButton = dialogView.findViewById(R.id.btn_okay);
            fileManager = dialogView.findViewById(R.id.button);
            imageButton = dialogView.findViewById(R.id.button3);
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
            dialog_text_change();
            alertDialog.show();
        });

        recyclerAdapter = new RecyclerAdapter(jsonArray,json2Array);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);

        setMenuItemStyle(item1, selectedItem, size);
        setMenuItemStyle(item2, selectedItem, size);
        setMenuItemStyle(item3, selectedItem, size);
        return true;
    }

    private void setMenuItemStyle(MenuItem item, String fontFamily, int textSize) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());

        spanString.setSpan(new TypefaceSpan(fontFamily), 0, spanString.length(), 0);
        spanString.setSpan(new TextAppearanceSpan(null, 0, textSize, null, null), 0, spanString.length(), 0);

        item.setTitle(spanString);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.item1){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        exit();
        return true;
    }

    public void exit(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);

        textView7 = dialogView.findViewById(R.id.textView7);
        textView5 = dialogView.findViewById(R.id.textView5);
        button2 = dialogView.findViewById(R.id.button2);
        button5 = dialogView.findViewById(R.id.button5);
        exit_text_change();
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        button2.setOnClickListener(v1 -> alertDialog.dismiss());
        button5.setOnClickListener(v12 -> {
        finishAffinity();
        });
        alertDialog.show();
    }


    private void save() {
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key", jsonArray.toString());
        editor.apply();
        Log.d("array_data", "save array data: " + jsonArray.toString());
    }


    private void load() {

        try {

            JSONObject jsonObject0 = json2Array.getJSONObject(0);

            selectedItem=jsonObject0.getString("style");
            size=jsonObject0.getInt("size");

            JSONObject jsonObject1 = json2Array.getJSONObject(1);
            orientation_value = jsonObject1.getString("value");

            if(orientation_value.equals("Sensor")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            if(orientation_value.equals("Portrate")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if(orientation_value.equals("Landscape")){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            main_text_change();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void dialog_text_change(){
        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        nameEditText.setTypeface(typeface);
        storageEditText.setTypeface(typeface);
        cancelButton.setTypeface(typeface);
        saveButton.setTypeface(typeface);
        textView3.setTypeface(typeface);
        textView2.setTypeface(typeface);


        nameEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        storageEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        saveButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);


    }
    public void exit_text_change(){
        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        textView7.setTypeface(typeface);
        textView5.setTypeface(typeface);
        button2.setTypeface(typeface);
        button5.setTypeface(typeface);

        textView7.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        button2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        button5.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }

    public void main_text_change(){
        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        addButton.setTypeface(typeface);

        addButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
    



}