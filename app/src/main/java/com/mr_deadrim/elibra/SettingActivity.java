package com.mr_deadrim.elibra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.transition.TransitionManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import android.transition.AutoTransition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SettingActivity extends AppCompatActivity {

    private View dialogView;
    ConstraintLayout arrow,arrow2,arrow4;
    ConstraintLayout hiddenView,hiddenView2,hiddenView4;
    CardView cardView,cardView2,cardView4;
    ImageView arrowimage,arrowimage2,arrowimage4,fileManager,imageView6;
    EditText editText;
    Button incrementButton,decrementButton;
    String selectedItem="sans-serif";
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private static final int CHOOSE_REQUEST_CODE = 1;
    Button textbutton,button7,button8;
    TextView textView,textView18,setting_title,parent2,parent4,button18,button19,textView8,textView23,textViewoutput;
    RadioButton radioButton,radioButton2,radioButton3;
    CheckBox checkbox,checkbox2,checkBox3;
    EditText editTextText,editTextText2,editTextText3;
    NestedScrollView textView20;
    public JSONArray jsonArray, jsonImportArray;
    String type="export",import_output="",export_output="";
    String export_path="/sdcard/Downloads/", import_path ="";
    private static final int BUFFER_SIZE = 4096;
    int equal=0,not_equal=0;
    String orientation_value="Sensor";
    int text_selected=1,orientation_selected=1,migration_selected=1,remove_existing_data_status,dont_import_status,dont_export_status;
    int text_size=40;
    JSONArray json2Array;

    TextView textView7;
    TextView textView5;

    Button button2;
    Button button5;

    MenuItem item1;
    MenuItem item2;
    MenuItem item3;
    EditText editTextText6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cardView = findViewById(R.id.base_cardview1);
        arrow = findViewById(R.id.fixed_layout1);
        arrowimage = findViewById(R.id.imageButton4);
        hiddenView = findViewById(R.id.hidden_view1);
        cardView2 = findViewById(R.id.base_cardview2);
        arrow2 = findViewById(R.id.fixed_layout2);
        arrowimage2 = findViewById(R.id.imageButton2);
        hiddenView2 = findViewById(R.id.hidden_view2);
        cardView4 = findViewById(R.id.base_cardview4);
        arrow4 = findViewById(R.id.fixed_layout4);
        arrowimage4 = findViewById(R.id.imageButton5);
        hiddenView4 = findViewById(R.id.hidden_view4);
        textbutton = findViewById(R.id.button6);
        textView = findViewById(R.id.textView19);
        textView18 = findViewById(R.id.textView18);
        setting_title = findViewById(R.id.textView22);
        parent2 = findViewById(R.id.parent2);
        parent4 = findViewById(R.id.parent4);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        button18 = findViewById(R.id.button18);
        button19 = findViewById(R.id.button19);
        textView8 = findViewById(R.id.textView8);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        button7 = findViewById(R.id.button7);
        checkbox = findViewById(R.id.checkBox);
        checkbox2 = findViewById(R.id.checkBox2);
        editTextText =findViewById(R.id.editTextText);
        button8=findViewById(R.id.button8);
        editTextText2 =findViewById(R.id.editTextText2);
        fileManager = findViewById(R.id.imageView3);
        textView23=findViewById(R.id.textView23);
        textView20=findViewById(R.id.textView20);
        textViewoutput=findViewById(R.id.textViewoutput);
        editTextText3=findViewById(R.id.editTextText3);
        editTextText6=findViewById(R.id.editTextText6);
        imageView6=findViewById(R.id.imageView6);
        checkBox3=findViewById(R.id.checkBox3);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView.getVisibility() == View.VISIBLE) {
                    hiddenView.setVisibility(View.GONE);
                    arrowimage.setImageResource(android.R.drawable.arrow_down_float);
                    text_selected=0;
                }else {
                    TransitionManager.beginDelayedTransition(cardView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);
                    arrowimage.setImageResource(android.R.drawable.arrow_up_float);
                    text_selected=1;
                }
            }
        });


        arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView2.getVisibility() == View.VISIBLE) {

                    hiddenView2.setVisibility(View.GONE);
                    arrowimage2.setImageResource(android.R.drawable.arrow_down_float);
                    orientation_selected=0;
                }else {
                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView2.setVisibility(View.VISIBLE);
                    arrowimage2.setImageResource(android.R.drawable.arrow_up_float);
                    orientation_selected=1;
                }
            }
        });



        arrow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView4.getVisibility() == View.VISIBLE) {

                    hiddenView4.setVisibility(View.GONE);
                    arrowimage4.setImageResource(android.R.drawable.arrow_down_float);
                    migration_selected=0;
                }else {

                    TransitionManager.beginDelayedTransition(cardView4,
                            new AutoTransition());
                    hiddenView4.setVisibility(View.VISIBLE);
                    arrowimage4.setImageResource(android.R.drawable.arrow_up_float);
                    migration_selected=1;
                }
            }
        });



        editText = findViewById(R.id.editTextText);
        incrementButton = findViewById(R.id.button19);
        decrementButton = findViewById(R.id.button18);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                try {
                    int value = Integer.parseInt(text);

                    incrementButton.setVisibility(value >= 200 ? View.INVISIBLE : View.VISIBLE);
                    decrementButton.setVisibility(value <= 11 ? View.INVISIBLE : View.VISIBLE);

                    settings_text_change();


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }



            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_size = Integer.parseInt(editText.getText().toString());
                if (text_size < 200) {
                    text_size = text_size + 10;
                    editText.setText(String.valueOf(text_size));
                }
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_size = Integer.parseInt(editText.getText().toString());
                if (text_size > 10) {
                    text_size = text_size - 10;
                    editText.setText(String.valueOf(text_size));
                }
            }
        });


        String[] fonts = getResources().getStringArray(R.array.android_fonts);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fonts);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteTextView.showDropDown();
            }
        });
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });


        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                settings_text_change();
            }
        });


        textbutton.setOnClickListener(v -> {
            type="import";
            migration(type);
        });

        button7.setOnClickListener(v -> {
            type="export";
            migration(type);
        });

        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                orientation_value = radioButton.getText().toString();

                if(orientation_value.equals("Sensor")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                if(orientation_value.equals("Portrate")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                if(orientation_value.equals("Landscape")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
            }
        });

        fileManager.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, FileManagerActivity.class);
            intent.putExtra("status", "select_folder");
            ((Activity) SettingActivity.this).startActivityForResult(intent, CHOOSE_REQUEST_CODE);
        });

        imageView6.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, FileManagerActivity.class);
            intent.putExtra("status", "select_zip_file");
            intent.putExtra("path", "/sdcard/Downloads");
            ((Activity) SettingActivity.this).startActivityForResult(intent, CHOOSE_REQUEST_CODE);
        });

        button8.setOnClickListener(view -> {

            File folderToZip = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + "/");
            File zippedFile = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + ".zip");
            SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            try {
                jsonArray = new JSONArray(prefs.getString("key", "[]"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                json2Array = new JSONArray(prefs.getString("key2", "[]"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            if(type.equals("export")) {
                textView20.setVisibility(View.VISIBLE);
                import_path = editTextText2.getText().toString();
                File folder = new File(Environment.getExternalStorageDirectory(), "E Libra");
                if (!folder.exists()) {
                    if (folder.mkdirs()) {
                        Toast.makeText(this, "Folder created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show();
                }

                try {
                    jsonImportArray = new JSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        JSONObject newJson = new JSONObject();
                        newJson.put("image_path", json.getString("image_path"));
                        newJson.put("name", json.getString("name"));
                        newJson.put("storage", json.getString("storage"));
                        copyFile(json.getString("storage"), editTextText2.getText().toString() + "/" + editTextText3.getText().toString() + "/" + json.getString("name") + "/" + json.getString("storage").substring(json.getString("storage").lastIndexOf("/") + 1));
                        copyFile(json.getString("image_path"), editTextText2.getText().toString() + "/" + editTextText3.getText().toString() + "/" + json.getString("name") + "/" + json.getString("image_path").substring(json.getString("image_path").lastIndexOf("/") + 1));
                        newJson.put("page", json.getString("page"));
                        newJson.put("total_pages", json.getString("total_pages"));
                        jsonImportArray.put(newJson);
                    }

                    migrationOutput("[ LIBRARY DATA STRUCTURE ]\n", type);
                    migrationOutput("\n\n" + formatJson(jsonImportArray) + "\n\n", type);
                    migrationOutput("[ SETTING DATA STRUCTURE ]\n", type);
                    migrationOutput("\n\n" + formatJson(json2Array) + "\n\n", type);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                File file1 = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + "/library-structure.json");


                try {
                    FileWriter fileWriter1 = new FileWriter(file1);
                    fileWriter1.write(jsonImportArray.toString());
                    fileWriter1.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to write or empty data to file", Toast.LENGTH_SHORT).show();
                }

                if (checkBox3.isChecked()){
                    File file2 = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + "/setting-structure.json");
                    try {
                        FileWriter fileWriter2 = new FileWriter(file2);
                        fileWriter2.write(json2Array.toString());
                        fileWriter2.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to write or empty data to file", Toast.LENGTH_SHORT).show();
                    }
                }

                if (zippedFile.exists()) {
                    Toast.makeText(getApplicationContext(), "File already exists", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(this, "zip function running", Toast.LENGTH_SHORT).show();
                        zip(folderToZip, zippedFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "error while files zipping", Toast.LENGTH_SHORT).show();
                    }
                }
                deleteFolder(folderToZip);
                migrationOutput("[ STATUS ]-( SUCCESS )\n\n[ PATH ] - "+zippedFile+"\n",type);

            }

            if(type.equals("import")){
                export_path =editTextText6.getText().toString();
                Toast.makeText(this, export_path, Toast.LENGTH_SHORT).show();

                if(checkbox.isChecked()){
                    jsonArray = new JSONArray();
                }
                if(!checkbox2.isChecked()){
                    json2Array = new JSONArray();
                }

                try {



                    if (unzip(new File(editTextText6.getText().toString()), new File("/sdcard/E Libra/"))) {
                        textView20.setVisibility(View.VISIBLE);






                    StringBuilder jsonData = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new FileReader("/sdcard/E Libra/library-structure.json"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonData.append(line);
                    }
                    reader.close();
                    JSONArray importjsonArray = new JSONArray(jsonData.toString());




                    for (int i = 0; i < importjsonArray.length(); i++) {
                        JSONObject json = importjsonArray.getJSONObject(i);
                        JSONObject newJson = new JSONObject();
                        if (json.getString("image_path").isEmpty()){
                            newJson.put("image_path", json.getString("image_path"));
                        }else{
                            newJson.put("image_path", "/sdcard/E Libra/Library/" + json.getString("name") + "/" + json.getString("image_path").substring(json.getString("image_path").lastIndexOf("/") + 1));
                        }
                        newJson.put("name",json.getString("name"));
                        if (json.getString("storage").isEmpty()) {
                            newJson.put("storage", json.getString("storage"));
                        }else{
                            newJson.put("storage", "/sdcard/E Libra/Library/" + json.getString("name") + "/" + json.getString("storage").substring(json.getString("storage").lastIndexOf("/") + 1));
                        }

                        newJson.put("page", json.getString("page"));
                        newJson.put("total_pages", json.getString("total_pages"));

                        equal++;
                        jsonArray.put(newJson);

                    }

                    migrationOutput("[ ANALYSED DATA ]",type);
                    migrationOutput("\n\n"+formatJson(importjsonArray),type);
                    migrationOutput("[ SETTINGS DATA ]",type);
                    migrationOutput("\n\n"+formatJson(json2Array),type);
                    migrationOutput("[ STATUS ]\n\nADDED - "+equal+"\n"+"TOTAL -"+importjsonArray.length()+"\n",type);



                    save();

                    } else {
                        Toast.makeText(this, "Path Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        });


        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    remove_existing_data_status=1;
                } else {
                    remove_existing_data_status=0;
                }
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox2.isChecked()) {
                    dont_import_status=1;
                } else {
                    dont_import_status=0;
                }
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox3.isChecked()) {
                    dont_export_status=1;
                } else {
                    dont_export_status=0;

                }
            }
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class); // Use getContext() if in a Fragment
                startActivity(intent);
                finish();
            }
        });

        load();

        migration(type);

    }


    private String formatJson(JSONArray jsonArray) throws JSONException {
        StringBuilder formattedJson = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            formattedJson.append(" [ ").append(i + 1).append(" ] ¬\n");
            for (int j = 0; j < jsonObject.names().length(); j++) {
                String key = jsonObject.names().getString(j);
                String value = jsonObject.getString(key);
                if(jsonArray.length()!=1) {
                    formattedJson.append("\t\t\t\t |---").append(key).append(" : ").append(value).append("\n");
                }else{
                    formattedJson.append("[---").append(key).append(" : ").append(value).append("\n");
                }
            }
            formattedJson.append("\n");
        }
        return formattedJson.toString();
    }

    private boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] children = folder.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteFolder(child);
                }
            }
        }
        return folder.delete();
    }

    public void migration(String type){
        if(type.equals("import")){
            import_path =editTextText2.getText().toString();

            textViewoutput.setText(import_output);
            textbutton.setEnabled(false);
            button7.setEnabled(true);
            textView23.setVisibility(View.GONE);
            editTextText3.setVisibility(View.GONE);
            editTextText2.setVisibility(View.GONE);
            fileManager.setVisibility(View.GONE);
            checkbox.setVisibility(View.VISIBLE);
            checkbox2.setVisibility(View.VISIBLE);

            editTextText6.setVisibility(View.VISIBLE);
            imageView6.setVisibility(View.VISIBLE);
            checkBox3.setVisibility(View.GONE);
        }
        if(type.equals("export")){
            export_path =editTextText6.getText().toString();

            textViewoutput.setText(export_output);
            button7.setEnabled(false);
            textbutton.setEnabled(true);
            textView23.setVisibility(View.VISIBLE);
            editTextText3.setVisibility(View.VISIBLE);
            editTextText2.setVisibility(View.VISIBLE);
            fileManager.setVisibility(View.VISIBLE);
            checkbox.setVisibility(View.GONE);
            checkbox2.setVisibility(View.GONE);

            editTextText6.setVisibility(View.GONE);
            imageView6.setVisibility(View.GONE);
            checkBox3.setVisibility(View.VISIBLE);
        }
    }
    public void migrationOutput(String string,String type){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(type.equals("import")){
            import_output+=string;
            textViewoutput.setText(import_output);
        }

        if(type.equals("export")){
            export_output+=string;
            textViewoutput.setText(export_output);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        item1 = menu.findItem(R.id.item1);
        item2 = menu.findItem(R.id.item2);
        item3 = menu.findItem(R.id.item3);

        setMenuItemStyle(item1, selectedItem, Integer.parseInt(editText.getText().toString()));
        setMenuItemStyle(item2, selectedItem, Integer.parseInt(editText.getText().toString()));
        setMenuItemStyle(item3, selectedItem, Integer.parseInt(editText.getText().toString()));

        item2.setEnabled(false);
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
            finish();
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
    public void exit(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
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
    public void settings_text_change(){

        SpannableString spanString1 = null;
        SpannableString spanString2 = null;
        SpannableString spanString3 = null;

        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        textView.setTypeface(typeface);
        textView18.setTypeface(typeface);
        textbutton.setTypeface(typeface);
        setting_title.setTypeface(typeface);
        textView8.setTypeface(typeface);
        parent2.setTypeface(typeface);
        parent4.setTypeface(typeface);
        autoCompleteTextView.setTypeface(typeface);
        button18.setTypeface(typeface);
        button19.setTypeface(typeface);
        button8.setTypeface(typeface);
        radioButton.setTypeface(typeface);
        radioButton2.setTypeface(typeface);
        radioButton3.setTypeface(typeface);
        button7.setTypeface(typeface);
        checkbox.setTypeface(typeface);
        checkbox2.setTypeface(typeface);
        editTextText.setTypeface(typeface);
        editTextText2.setTypeface(typeface);
        editTextText3.setTypeface(typeface);
        textView23.setTypeface(typeface);
        checkBox3.setTypeface(typeface);
        editTextText6.setTypeface(typeface);
        textViewoutput.setTypeface(typeface);

        if (item1 != null && item1.getTitle() != null) {
            spanString1 = new SpannableString(item1.getTitle().toString());
        }
        if (item2 != null && item2.getTitle() != null) {
            spanString2 = new SpannableString(item2.getTitle().toString());
        }
        if (item3 != null && item3.getTitle() != null) {
            spanString3 = new SpannableString(item3.getTitle().toString());
        }

        if (spanString1 != null) {
            spanString1.setSpan(new TypefaceSpan(selectedItem), 0, spanString1.length(), 0);
        }
        if (spanString2 != null) {
            spanString2.setSpan(new TypefaceSpan(selectedItem), 0, spanString2.length(), 0);
        }
        if (spanString3 != null) {
            spanString3.setSpan(new TypefaceSpan(selectedItem), 0, spanString3.length(), 0);
        }

        int newTextSize = Integer.parseInt(editText.getText().toString());
        if(newTextSize>=10) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textView18.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textbutton.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            setting_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            parent2.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            parent4.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            button18.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            button19.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            button8.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textView8.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            radioButton2.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            radioButton3.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            button7.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            checkbox.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            checkbox2.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextText.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextText3.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textView23.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            checkBox3.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextText6.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewoutput.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            if (spanString1 != null) {
                spanString1.setSpan(new TextAppearanceSpan(null, 0, newTextSize, null, null), 0, spanString1.length(), 0);
            }
            if (spanString2 != null) {
                spanString2.setSpan(new TextAppearanceSpan(null, 0, newTextSize, null, null), 0, spanString2.length(), 0);
            }
            if (spanString3 != null) {
                spanString3.setSpan(new TextAppearanceSpan(null, 0, newTextSize, null, null), 0, spanString3.length(), 0);
            }

        }
        if (item1 != null && item1.getTitle() != null) {
            item1.setTitle(spanString1);
        }
        if (item2 != null && item2.getTitle() != null) {
            item2.setTitle(spanString2);
        }
        if (item3 != null && item3.getTitle() != null) {
            item3.setTitle(spanString3);
        }
    }
    public void exit_text_change(){
        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        textView7.setTypeface(typeface);
        textView5.setTypeface(typeface);
        button2.setTypeface(typeface);
        button5.setTypeface(typeface);
        int newTextSize = Integer.parseInt(editText.getText().toString());
        if(newTextSize>=10) {
            textView7.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textView5.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            button2.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            button5.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        }

    }
    public void copyFile(String sourcePath, String destPath) {
        File source = new File(sourcePath);
        File destination = new File(destPath);
        destination.getParentFile().mkdirs();

        if (!source.exists()) {
            Toast.makeText(SettingActivity.this, "Source file does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            Toast.makeText(this, "File copied Successfully: "+destPath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String filePath = data.getStringExtra("path");

            if (requestCode == 1){
                if(type.equals("import")){
                    editTextText6.setText(filePath);
                }
                if(type.equals("export")){
                    editTextText2.setText(filePath);
                }
            }

        }
    }
    private void zip(File folder, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    zipFile(file, file.getName(), zos);
                }
            }
        }
    }
    private void zipFile(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.closeEntry();
            } else {
                zos.putNextEntry(new ZipEntry(fileName + "/"));
                zos.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zos);
                }
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
        }
    }
    public boolean unzip(File zipFile, File destDir) {
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                Toast.makeText(this, "Failed to create destination directory.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                try {
                    File newFile = newFile(destDir, entry);
                    if (entry.isDirectory()) {
                        if (!newFile.mkdirs()) {
                            throw new IOException("Failed to create directory: " + newFile.getAbsolutePath());
                        }
                    } else {
                        File parent = newFile.getParentFile();
                        if (!parent.isDirectory()) {
                            throw new IOException("Failed to create directory: " + parent.getAbsolutePath());
                        }
                        try (FileOutputStream fos = new FileOutputStream(newFile)) {
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int length;
                            while ((length = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error processing entry: " + entry.getName() + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    return false;
                }
                zis.closeEntry();
                entry = zis.getNextEntry();
            }
            return true;
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Zip file not found: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } catch (IOException e) {
            Toast.makeText(this, "Error reading zip file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private File newFile(File destDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destDir, zipEntry.getName());
        String destDirPath = destDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
    @Override
    protected void onPause() {
        super.onPause();
        save_setting();

    }

    private void load() {

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
       try {
           json2Array = new JSONArray(prefs.getString("key2", "[]"));
           JSONObject jsonObject0 = json2Array.getJSONObject(0);

            text_selected = jsonObject0.getInt("selected");
            selectedItem=jsonObject0.getString("style");
            autoCompleteTextView.setText(selectedItem, false);
            editText.setText(String.valueOf(jsonObject0.getInt("size")));
            JSONObject jsonObject1 = json2Array.getJSONObject(1);
            orientation_selected = jsonObject1.getInt("selected");
            orientation_value = jsonObject1.getString("value");

            JSONObject jsonObject2 = json2Array.getJSONObject(2);
            migration_selected = jsonObject2.getInt("selected");
            type = jsonObject2.getString("type");
            editTextText3.setText(jsonObject2.getString("export_name"));
            export_path = jsonObject2.getString("export_path");
            editTextText2.setText(export_path);
            dont_export_status = jsonObject2.getInt("export_setting_status");

            import_path = jsonObject2.getString("import_path");
            editTextText6.setText(import_path);
            dont_import_status = jsonObject2.getInt("import_setting_status");
            remove_existing_data_status = jsonObject2.getInt("remove_existing_data_status");

            if(text_selected==0){
                hiddenView.setVisibility(View.GONE);
                arrowimage.setImageResource(android.R.drawable.arrow_down_float);
            }else{
                hiddenView.setVisibility(View.VISIBLE);
                arrowimage.setImageResource(android.R.drawable.arrow_up_float);
            }
            if(orientation_selected==0){
                hiddenView2.setVisibility(View.GONE);
                arrowimage2.setImageResource(android.R.drawable.arrow_down_float);
            }else{
                hiddenView2.setVisibility(View.VISIBLE);
                arrowimage2.setImageResource(android.R.drawable.arrow_up_float);
            }
            if (migration_selected==0) {
                hiddenView4.setVisibility(View.GONE);
                arrowimage4.setImageResource(android.R.drawable.arrow_down_float);
            }else {
                hiddenView4.setVisibility(View.VISIBLE);
                arrowimage4.setImageResource(android.R.drawable.arrow_up_float);
            }
            if(orientation_value.equals("Sensor")){
                radioButton.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            if(orientation_value.equals("Portrate")){
                radioButton2.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if(orientation_value.equals("Landscape")){
                radioButton3.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }


            if(remove_existing_data_status==1){
                checkbox.setChecked(true);
            }else{
                checkbox.setChecked(false);
            }

            if(dont_import_status==1){
                checkbox2.setChecked(true);
            }else{
                checkbox2.setChecked(false);
            }

           if(dont_export_status==1){
               checkBox3.setChecked(true);
           }else{
               checkBox3.setChecked(false);
           }

       }catch (Exception e){
           e.printStackTrace();
       }

    }

    private void save_setting() {
        json2Array = new JSONArray();
        int textSize = Integer.parseInt(editText.getText().toString());
        if (textSize < 10) {
            textSize = 40;
            }

        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("selected", text_selected);
            jsonObject1.put("size", textSize);
            jsonObject1.put("style", selectedItem);
            json2Array.put(0, jsonObject1);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("selected", orientation_selected);
            jsonObject2.put("value", orientation_value);
            json2Array.put(1, jsonObject2);

            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("selected", migration_selected);
            jsonObject3.put("type", type);
            jsonObject3.put("export_name", editTextText3.getText().toString());
            jsonObject3.put("export_path", editTextText2.getText().toString());
            jsonObject3.put("export_setting_status", dont_export_status);
            jsonObject3.put("import_path", editTextText6.getText().toString());
            jsonObject3.put("import_setting_status", dont_import_status);
            jsonObject3.put("remove_existing_data_status", remove_existing_data_status);
            json2Array.put(2, jsonObject3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key2", json2Array.toString());
        editor.apply();
    }

    private void save() {
        Log.d("book_array",jsonArray.toString());
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key", jsonArray.toString());
        editor.apply();
        Log.d("array_data", "save array data: " + jsonArray.toString());
    }


}


