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
import android.text.TextWatcher;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.transition.TransitionManager;

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
    ConstraintLayout arrow,arrow2,arrow3,arrow4;
    ConstraintLayout hiddenView,hiddenView2,hiddenView3,hiddenView4;
    CardView cardView,cardView2,cardView3,cardView4;
    ImageView arrowimage,arrowimage2,arrowimage3,arrowimage4;
    EditText editText;
    Button incrementButton,decrementButton;
    String selectedItem="sans-serif";

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    private static final int CHOOSE_REQUEST_CODE = 1;


    Button textbutton,button7,button8;
    TextView textView,textView18,setting_title,parent2,parent3,parent4,button18,button19,textView8,textView23,textViewoutput;
    RadioButton  radioButton,radioButton2,radioButton3,radioButtonSortBYName,radioButtonSortBYDate;
    CheckBox checkbox;
    EditText editTextText,editTextText2,editTextText3;

    NestedScrollView textView20;

    public JSONArray jsonArray, jsonImportArray;

    String type="export",import_output="",export_output="";
    String export_path="/sdcard/Downloads/", import_path ="";
    private static final int BUFFER_SIZE = 4096;
    ScrollView scrollview;
    int equal=0,not_equal=0;

    String radioButtonValue="Sensor",sort_order="by_date";
    
    int text_selected=1,orientation_selected=1,sort_order_selected=1,migration_selected=1;

    int text_size=30;

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

        cardView3 = findViewById(R.id.base_cardview3);
        arrow3 = findViewById(R.id.fixed_layout3);
        arrowimage3 = findViewById(R.id.imageButton3);
        hiddenView3 = findViewById(R.id.hidden_view3);

        cardView4 = findViewById(R.id.base_cardview4);
        arrow4 = findViewById(R.id.fixed_layout4);
        arrowimage4 = findViewById(R.id.imageButton5);
        hiddenView4 = findViewById(R.id.hidden_view4);





//rest of the buttons
        textbutton=findViewById(R.id.button6);
        textView=findViewById(R.id.textView19);
        textView18=findViewById(R.id.textView18);
        setting_title=findViewById(R.id.textView22);

        parent2=findViewById(R.id.parent2);
        parent3=findViewById(R.id.parent3);
        parent4=findViewById(R.id.parent4);
        autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
        button18=findViewById(R.id.button18);
        button19=findViewById(R.id.button19);
        textView8=findViewById(R.id.textView8);


        radioButton =findViewById(R.id.radioButton);
        radioButton2 =findViewById(R.id.radioButton2);
        radioButton3 =findViewById(R.id.radioButton3);


        radioButtonSortBYName = findViewById(R.id.radioButtonSortBYName);
        radioButtonSortBYDate = findViewById(R.id.radioButtonSortBYDate);

        button7=findViewById(R.id.button7);

        checkbox = findViewById(R.id.checkBox);
        editTextText =findViewById(R.id.editTextText);
        button8=findViewById(R.id.button8);
        editTextText2 =findViewById(R.id.editTextText2);



        ImageView fileManager = findViewById(R.id.imageView3);


        editTextText3=findViewById(R.id.editTextText3);
        textView23=findViewById(R.id.textView23);
        textView20=findViewById(R.id.textView20);

        textViewoutput=findViewById(R.id.textViewoutput);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView.getVisibility() == View.VISIBLE) {
                    hiddenView.setVisibility(View.GONE);
                    arrowimage.setImageResource(android.R.drawable.arrow_down_float);
                    text_selected=0;
                }

                else {
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
                }

                else {
                    TransitionManager.beginDelayedTransition(cardView2,
                            new AutoTransition());
                    hiddenView2.setVisibility(View.VISIBLE);
                    arrowimage2.setImageResource(android.R.drawable.arrow_up_float);
                    orientation_selected=1;
                }
            }
        });


        arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenView3.getVisibility() == View.VISIBLE) {
                    hiddenView3.setVisibility(View.GONE);
                    arrowimage3.setImageResource(android.R.drawable.arrow_down_float);
                    sort_order_selected=0;
                }

                else {

                    TransitionManager.beginDelayedTransition(cardView3,
                            new AutoTransition());
                    hiddenView3.setVisibility(View.VISIBLE);
                    arrowimage3.setImageResource(android.R.drawable.arrow_up_float);
                    sort_order_selected=1;
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
                }

                else {

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


                if (text.isEmpty()) {
                    editText.setText("0");
                    return;
                }


                try {
                    int value = Integer.parseInt(text);
                    if (value < 1) {
                        editText.setText("1");
                    }
                    if (value > 200) {
                        editText.setText("200");
                    }


                    incrementButton.setVisibility(value >= 200 ? View.INVISIBLE : View.VISIBLE);
                    decrementButton.setVisibility(value <= 1 ? View.INVISIBLE : View.VISIBLE);


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
                if (text_size > 1) {
                    text_size = text_size - 10;
                    editText.setText(String.valueOf(text_size));
                }
            }
        });

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
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


        migration(type);



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
                radioButtonValue = radioButton.getText().toString();

                if(radioButtonValue.equals("Sensor")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                if(radioButtonValue.equals("Portrate")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                if(radioButtonValue.equals("Landscape")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String radioButtonValue = radioButton.getText().toString();

                if(radioButtonValue.equals("Sort BY Name")){
                    sort_order="by_name";
                    Toast.makeText(SettingActivity.this, "1", Toast.LENGTH_SHORT).show();

                }
                if(radioButtonValue.equals("Sort By Date")){
                    sort_order="by_date";
                    Toast.makeText(SettingActivity.this, "2", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fileManager.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, FileManagerActivity.class);
            if(type.equals("import")){
                intent.putExtra("status", "select_zip_file");
            }
            if(type.equals("export")){
                intent.putExtra("status", "select_folder");
            }

            ((Activity) SettingActivity.this).startActivityForResult(intent, CHOOSE_REQUEST_CODE);

        });

        button8.setOnClickListener(view -> {
            textView20.setVisibility(View.VISIBLE);
            File folderToZip = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + "/");
            File zippedFile = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + ".zip");


            SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);

            try {
                jsonArray = new JSONArray(prefs.getString("key", "[]"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            if(type.equals("export")) {
                import_path =editTextText2.getText().toString();
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
                        newJson.put("name",json.getString("name"));
                        newJson.put("storage", json.getString("storage"));
                        copyFile(json.getString("storage"), editTextText2.getText().toString() + "/" + editTextText3.getText().toString() + "/" + json.getString("name") + "/" + json.getString("storage").substring(json.getString("storage").lastIndexOf("/") + 1));
                        copyFile(json.getString("image_path"), editTextText2.getText().toString() + "/" + editTextText3.getText().toString() + "/" + json.getString("name") + "/" + json.getString("image_path").substring(json.getString("image_path").lastIndexOf("/") + 1));
                        newJson.put("page", json.getString("page"));
                        newJson.put("total_pages", json.getString("total_pages"));
                        jsonImportArray.put(newJson);
                    }


                    migrationOutput("[ JSON DATA STRUCTURE ]\n",type);
                    migrationOutput("\n\n"+formatJson(jsonImportArray)+"\n\n",type);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                File file = new File(editTextText2.getText().toString(), "/" + editTextText3.getText().toString() + "/library-structure.json");
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(jsonImportArray.toString());
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to write data to file", Toast.LENGTH_SHORT).show();
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


                migrationOutput("[ STATUS ]-(success)\n\n[ PATH ] - "+zippedFile+"\n",type);

            }

            if(type.equals("import")){
                export_path =editTextText2.getText().toString();
                String modifiedFilePath = editTextText2.getText().toString().replace(".zip","");
                Toast.makeText(this, modifiedFilePath, Toast.LENGTH_SHORT).show();
                try {

                    unzip(new File(editTextText2.getText().toString()), new File("/sdcard/E Libra/"));
                    Toast.makeText(this, modifiedFilePath.substring(modifiedFilePath.lastIndexOf('/') + 1), Toast.LENGTH_SHORT).show();

                    StringBuilder jsonData = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new FileReader("/sdcard/E Libra/library-structure.json"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonData.append(line);
                    }
                    reader.close();
                    JSONArray importjsonArray = new JSONArray(jsonData.toString());

                    Set<String> uniqueKeys = new HashSet<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String key = json.getString("image_path") + "|" +
                                json.getString("name") + "|" +
                                json.getString("storage") + "|" +
                                json.getString("page") + "|" +
                                json.getString("total_pages");
                        uniqueKeys.add(key);
                    }

                    for (int i = 0; i < importjsonArray.length(); i++) {
                        JSONObject json = importjsonArray.getJSONObject(i);
                        String key = json.getString("image_path") + "|" +
                                json.getString("name") + "|" +
                                json.getString("storage") + "|" +
                                json.getString("page") + "|" +
                                json.getString("total_pages");

                        if (!uniqueKeys.contains(key)) {

                            uniqueKeys.add(key);
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

                            equal++;
                            jsonArray.put(newJson);
                        }
                    }

                    not_equal=importjsonArray.length()-equal;
                    migrationOutput("[ ANALYSED DATA ]",type);
                    migrationOutput("\n\n"+formatJson(importjsonArray),type);
                    migrationOutput("[ STATUS ]\n\nDUPLICATION - "+not_equal+"\n"+"ADDED - "+equal+"\n"+"TOTAL -"+importjsonArray.length()+"\n",type);

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }

    private String formatJson(JSONArray jsonArray) throws JSONException {
        StringBuilder formattedJson = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            formattedJson.append(" [ ") .append(i + 1).append(" ] ¬\n");
            for (int j = 0; j < jsonObject.names().length(); j++) {
                String key = jsonObject.names().getString(j);
                String value = jsonObject.getString(key);
                formattedJson.append("\t\t\t |---").append(key).append(" : ").append(value).append("\n");
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

            export_path =editTextText2.getText().toString();
            editTextText2.setText(import_path);

            textViewoutput.setText(export_output);
            textbutton.setEnabled(false);
            button7.setEnabled(true);

            textView23.setVisibility(View.INVISIBLE);
            editTextText3.setVisibility(View.GONE);
            checkbox.setVisibility(View.VISIBLE);
        }
        if(type.equals("export")){

            import_path =editTextText2.getText().toString();
            editTextText2.setText(export_path);

            textViewoutput.setText(export_output);
            button7.setEnabled(false);
            textbutton.setEnabled(true);

            textView23.setVisibility(View.VISIBLE);
            editTextText3.setVisibility(View.VISIBLE);

            checkbox.setVisibility(View.GONE);
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

        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);


        item2.setEnabled(false);
        return true;
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
        Button cancelButton = dialogView.findViewById(R.id.button2);
        Button exitButton = dialogView.findViewById(R.id.button5);
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        cancelButton.setOnClickListener(v1 -> alertDialog.dismiss());
        exitButton.setOnClickListener(v12 -> {
            finishAffinity();
        });
        alertDialog.show();
    }
    public void settings_text_change(){

        Typeface typeface = Typeface.create(selectedItem, Typeface.NORMAL);
        textView.setTypeface(typeface);
        textView18.setTypeface(typeface);
        textbutton.setTypeface(typeface);
        setting_title.setTypeface(typeface);
        textView8.setTypeface(typeface);

        parent2.setTypeface(typeface);
        parent3.setTypeface(typeface);
        parent4.setTypeface(typeface);
        autoCompleteTextView.setTypeface(typeface);
        button18.setTypeface(typeface);
        button19.setTypeface(typeface);
        button8.setTypeface(typeface);

        radioButton.setTypeface(typeface);
        radioButton2.setTypeface(typeface);
        radioButton3.setTypeface(typeface);

        radioButtonSortBYName.setTypeface(typeface);
        radioButtonSortBYDate.setTypeface(typeface);

        button7.setTypeface(typeface);
        checkbox.setTypeface(typeface);

        editTextText.setTypeface(typeface);

        editTextText2.setTypeface(typeface);




        float newTextSize = Float.parseFloat(editText.getText().toString());


        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        textView18.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        textbutton.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        setting_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        parent2.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        parent3.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        parent4.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        button18.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        button19.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        button8.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        textView8.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        radioButton2.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        radioButton3.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        radioButtonSortBYName.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        radioButtonSortBYDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        button7.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        editTextText.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
        editTextText2.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);
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
                editTextText2.setText(filePath);
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
    private void unzip(File zipFile, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                File newFile = newFile(destDir, entry);
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
                entry = zis.getNextEntry();
            }
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
        save();
    }

    private void save() {
        Toast.makeText(this, "settings saved", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences("MyAppSettings", MODE_PRIVATE);
        try {
            JSONObject settings = new JSONObject();

            JSONObject text = new JSONObject();
            text.put("selected", text_selected);
            text.put("style", selectedItem);
            text.put("size", editText.getText().toString());

            JSONObject orientation = new JSONObject();
            orientation.put("selected", orientation_selected);
            orientation.put("value", radioButtonValue);

            JSONObject sortOrder = new JSONObject();
            sortOrder.put("selected", sort_order_selected);
            sortOrder.put("value", sort_order);

            JSONObject migration = new JSONObject();
            migration.put("selected", migration_selected);
            migration.put("type", type);
            migration.put("export_name", editTextText3.getText().toString());
            migration.put("export_path", export_path);
            migration.put("import_path", import_path);


            settings.put("text", text);
            settings.put("orientation", orientation);
            settings.put("sort_order", sortOrder);
            settings.put("migration", migration);


            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("settings", settings.toString());
            editor.apply();

            Log.d("json_setting",settings.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


