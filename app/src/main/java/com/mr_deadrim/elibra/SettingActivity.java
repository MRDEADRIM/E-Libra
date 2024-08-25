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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SettingActivity extends AppCompatActivity {

    private View dialogView,fileReplaceDialog;
    ConstraintLayout constraintLayoutTextFixedLayout, constraintLayoutOrientationFixedLayout, constraintLayoutMigrationFixedLayout, constraintLayoutHiddenView, constraintLayoutOrientationHiddenView, constraintLayoutMigrationHiddenView;
    CardView cardViewText, cardViewOrientation, cardViewMigration;
    ImageView imageViewTextToggle, imageViewOrientationToggle, imageViewMigrationToggle, imageViewFolderPicker, imageViewFilePicker;
    EditText editTextSize,editTextExportPath, editTextExportFile,editTextImportPath;
    Button buttonSizeDecrement, buttonSizeIncrement,buttonImport, buttonExport, buttonProceed, buttonExitNo, buttonExitYes,buttonFileReplaceNo, buttonFileReplaceYes;
    String textStyle="sans-serif", orientationValue ="Sensor",type="export",import_output="",export_output="",export_path="/sdcard/Downloads/", import_path ="";
    private AutoCompleteTextView autoCompleteTextViewStyle;
    private ArrayAdapter<String> adapter;
    private static final int CHOOSE_REQUEST_CODE = 1;
    TextView textViewStyle, textViewAdjustSize, textViewSettings, textViewOrientation, textViewMigration, textViewText, textViewZip, textViewOutputPreview, textViewExit, textViewExitMessage,textViewFileReplace, textViewFileReplaceMessage;
    RadioButton radioButtonSensor, radioButtonPortrait, radioButtonLandscape;
    CheckBox checkBoxRemoveExisting, checkBoxImportSettings, checkBoxExportSettings;
    NestedScrollView nestedScrollViewStatusOutput;
    public JSONArray libraryJsonArray,settingJsonArray, jsonImportArray;
    private static final int BUFFER_SIZE = 4096;
    int added=0, textSize =40,text_selected=1,orientation_selected=1,migration_selected=1,remove_existing_data_status,dont_import_status,dont_export_status;
    MenuItem item1,item2,item3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cardViewText = findViewById(R.id.cardViewText);
        constraintLayoutTextFixedLayout = findViewById(R.id.constraintLayoutTextFixedLayout);
        imageViewTextToggle = findViewById(R.id.imageViewTextToggle);
        constraintLayoutHiddenView = findViewById(R.id.constraintLayoutHiddenView);
        cardViewOrientation = findViewById(R.id.cardViewOrientation);
        constraintLayoutOrientationFixedLayout = findViewById(R.id.constraintLayoutOrientationFixedLayout);
        imageViewOrientationToggle = findViewById(R.id.imageViewOrientationToggle);
        constraintLayoutOrientationHiddenView = findViewById(R.id.constraintLayoutOrientationHiddenView);
        cardViewMigration = findViewById(R.id.cardViewMigration);
        constraintLayoutMigrationFixedLayout = findViewById(R.id.constraintLayoutMigrationFixedLayout);
        imageViewMigrationToggle = findViewById(R.id.imageViewMigrationToggle);
        constraintLayoutMigrationHiddenView = findViewById(R.id.constraintLayoutMigrationHiddenView);
        buttonImport = findViewById(R.id.buttonImport);
        textViewStyle = findViewById(R.id.textViewStyle);
        textViewAdjustSize = findViewById(R.id.textViewAdjustSize);
        textViewSettings = findViewById(R.id.textViewSettings);
        textViewOrientation = findViewById(R.id.textViewOrientation);
        textViewMigration = findViewById(R.id.textViewMigration);
        autoCompleteTextViewStyle = findViewById(R.id.autoCompleteTextViewStyle);
        buttonSizeIncrement = findViewById(R.id.buttonSizeIncrement);
        buttonSizeDecrement = findViewById(R.id.buttonSizeDecrement);
        textViewText = findViewById(R.id.textViewText);
        radioButtonSensor = findViewById(R.id.radioButtonSensor);
        radioButtonPortrait = findViewById(R.id.radioButtonPortrait);
        radioButtonLandscape = findViewById(R.id.radioButtonLandscape);
        buttonExport = findViewById(R.id.buttonExport);
        checkBoxRemoveExisting = findViewById(R.id.checkBoxRemoveExisting);
        checkBoxImportSettings = findViewById(R.id.checkBoxImportSettings);
        editTextSize = findViewById(R.id.editTextSize);
        buttonProceed = findViewById(R.id.buttonProceed);
        editTextExportPath = findViewById(R.id.editTextExportPath);
        imageViewFolderPicker = findViewById(R.id.imageViewFolderPicker);
        textViewZip = findViewById(R.id.textViewZip);
        nestedScrollViewStatusOutput = findViewById(R.id.nestedScrollViewStatusOutput);
        textViewOutputPreview = findViewById(R.id.textViewOutputPreview);
        editTextExportFile = findViewById(R.id.editTextExportFile);
        editTextImportPath = findViewById(R.id.editTextImportPath);
        imageViewFilePicker = findViewById(R.id.imageViewFilePicker);
        checkBoxExportSettings = findViewById(R.id.checkBoxExportSettings);
        constraintLayoutTextFixedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (constraintLayoutHiddenView.getVisibility() == View.VISIBLE) {
                    constraintLayoutHiddenView.setVisibility(View.GONE);
                    imageViewTextToggle.setImageResource(android.R.drawable.arrow_down_float);
                    text_selected=0;
                }else {
                    TransitionManager.beginDelayedTransition(cardViewText, new AutoTransition());
                    constraintLayoutHiddenView.setVisibility(View.VISIBLE);
                    imageViewTextToggle.setImageResource(android.R.drawable.arrow_up_float);
                    text_selected=1;
                }
            }
        });
        constraintLayoutOrientationFixedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (constraintLayoutOrientationHiddenView.getVisibility() == View.VISIBLE) {
                    constraintLayoutOrientationHiddenView.setVisibility(View.GONE);
                    imageViewOrientationToggle.setImageResource(android.R.drawable.arrow_down_float);
                    orientation_selected=0;
                }else {
                    TransitionManager.beginDelayedTransition(cardViewOrientation,new AutoTransition());
                    constraintLayoutOrientationHiddenView.setVisibility(View.VISIBLE);
                    imageViewOrientationToggle.setImageResource(android.R.drawable.arrow_up_float);
                    orientation_selected=1;
                }
            }
        });
        constraintLayoutMigrationFixedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (constraintLayoutMigrationHiddenView.getVisibility() == View.VISIBLE) {
                    constraintLayoutMigrationHiddenView.setVisibility(View.GONE);
                    imageViewMigrationToggle.setImageResource(android.R.drawable.arrow_down_float);
                    migration_selected=0;
                }else {
                    TransitionManager.beginDelayedTransition(cardViewMigration, new AutoTransition());
                    constraintLayoutMigrationHiddenView.setVisibility(View.VISIBLE);
                    imageViewMigrationToggle.setImageResource(android.R.drawable.arrow_up_float);
                    migration_selected=1;
                }
            }
        });
        editTextSize.addTextChangedListener(new TextWatcher() {
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
                    buttonSizeDecrement.setVisibility(value >= 200 ? View.INVISIBLE : View.VISIBLE);
                    buttonSizeIncrement.setVisibility(value <= 11 ? View.INVISIBLE : View.VISIBLE);
                    settings_text_change();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonSizeDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize = Integer.parseInt(editTextSize.getText().toString());
                if (textSize < 200) {
                    textSize = textSize + 10;
                    editTextSize.setText(String.valueOf(textSize));
                }
            }
        });
        buttonSizeIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize = Integer.parseInt(editTextSize.getText().toString());
                if (textSize > 10) {
                    textSize = textSize - 10;
                    editTextSize.setText(String.valueOf(textSize));
                }
            }
        });
        String[] fonts = getResources().getStringArray(R.array.android_fonts);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fonts);
        autoCompleteTextViewStyle.setAdapter(adapter);
        autoCompleteTextViewStyle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteTextViewStyle.showDropDown();
            }
        });
        autoCompleteTextViewStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextViewStyle.showDropDown();
            }
        });
        autoCompleteTextViewStyle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        autoCompleteTextViewStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textStyle = (String) parent.getItemAtPosition(position);
                settings_text_change();
            }
        });
        buttonImport.setOnClickListener(v -> {
            type="import";
            migration(type);
        });
        buttonExport.setOnClickListener(v -> {
            type="export";
            migration(type);
        });

        RadioGroup radioGroup2 = findViewById(R.id.radioGroupOrientation);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                orientationValue = radioButton.getText().toString();
                if(orientationValue.equals("Sensor")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                if(orientationValue.equals("Portrait")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                if(orientationValue.equals("Landscape")){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
            }
        });
        imageViewFolderPicker.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, FileManagerActivity.class);
            intent.putExtra("status", "select_folder");
            ((Activity) SettingActivity.this).startActivityForResult(intent, CHOOSE_REQUEST_CODE);
        });
        imageViewFilePicker.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, FileManagerActivity.class);
            intent.putExtra("status", "select_zip_file");
            ((Activity) SettingActivity.this).startActivityForResult(intent, CHOOSE_REQUEST_CODE);
        });
        buttonProceed.setOnClickListener(view -> {
            File folderToZip = new File(editTextExportPath.getText().toString(), "/" + editTextExportFile.getText().toString() + "/");
            File zippedFile = new File(editTextExportPath.getText().toString(), "/" + editTextExportFile.getText().toString() + ".zip");
            SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
            try {
                libraryJsonArray = new JSONArray(prefs.getString("libraryJsonArray", "[]"));

                settingJsonArray = new JSONArray(prefs.getString("settingJsonArray", "[]"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            File folder = new File(Environment.getExternalStorageDirectory(), "E Libra");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if(type.equals("export")) {

                    File file = new File(editTextExportPath.getText().toString(), editTextExportFile.getText().toString());
                    if (!file.exists() && !file.mkdirs()) {
                        Toast.makeText(this, "Failed to create directory: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                        import_path = editTextExportPath.getText().toString();
                        try {
                            jsonImportArray = new JSONArray();
                            for (int i = 0; i < libraryJsonArray.length(); i++) {
                                JSONObject json = libraryJsonArray.getJSONObject(i);
                                JSONObject newJson = new JSONObject();
                                newJson.put("image_path", json.getString("image_path"));
                                newJson.put("name", json.getString("name"));
                                newJson.put("storage", json.getString("storage"));
                                copyFile(json.getString("storage"), editTextExportPath.getText().toString() + "/" + editTextExportFile.getText().toString() + "/" + json.getString("name") + "/" + json.getString("storage").substring(json.getString("storage").lastIndexOf("/") + 1));
                                copyFile(json.getString("image_path"), editTextExportPath.getText().toString() + "/" + editTextExportFile.getText().toString() + "/" + json.getString("name") + "/" + json.getString("image_path").substring(json.getString("image_path").lastIndexOf("/") + 1));
                                newJson.put("page", json.getInt("page"));
                                newJson.put("total_pages", json.getInt("total_pages"));
                                jsonImportArray.put(newJson);
                            }
                            if (jsonImportArray.length() > 0) {
                                migrationOutput("[ LIBRARY DATA STRUCTURE ]\n", type);
                                migrationOutput("\n\n" + formatJson(jsonImportArray) + "\n\n", type);
                            }
                            if (checkBoxExportSettings.isChecked()){
                                nestedScrollViewStatusOutput.setVisibility(View.VISIBLE);
                                migrationOutput("[ SETTING DATA STRUCTURE ]\n", type);
                                migrationOutput("\n\n" + formatJson(settingJsonArray) + "\n\n", type);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    if (libraryJsonArray != null && libraryJsonArray.length() > 0) {
                        try (FileWriter fileWriter = new FileWriter(new File(file, "library-structure.json"))) {
                            fileWriter.write(libraryJsonArray.toString());
                            Log.d("log_message","File written successfully: " + file.getAbsolutePath());
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to write to file", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    if (checkBoxExportSettings.isChecked()) {
                        if (settingJsonArray != null && settingJsonArray.length() > 0) {
                            try (FileWriter fileWriter = new FileWriter(new File(file, "setting-structure.json"))) {
                                fileWriter.write(settingJsonArray.toString());
                                Log.d("log_message","File written successfully: " + file.getAbsolutePath());
                            } catch (Exception e) {
                                Toast.makeText(this, "Failed to write to file", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    if (zippedFile.exists()) {
                        if(libraryJsonArray != null && libraryJsonArray.length() > 0 || checkBoxExportSettings.isChecked()){
                            file_replace_dialog(folderToZip, zippedFile);
                        }else{
                            Toast.makeText(this, "Nothing to Export", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Toast.makeText(this, "Exporting...", Toast.LENGTH_SHORT).show();
                            zip(folderToZip, zippedFile);
                            deleteFolder(folderToZip);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "error while files zipping", Toast.LENGTH_SHORT).show();
                        }
                        migrationOutput("[ STATUS ]-( SUCCESS )\n\n[ GENERATED PATH ] - " + zippedFile + "\n\n", type);
                    }
            }
            if(type.equals("import")) {
                export_path = editTextImportPath.getText().toString();
                if (checkBoxRemoveExisting.isChecked()) {
                    libraryJsonArray = new JSONArray();
                    added = 0;
                }


                File file1 = new File("/sdcard/E Libra/Library/library-structure.json");
                if (file1.exists()) {
                    if (file1.delete()) {
                        Log.d("log_message","removed previous file library-structure.json");
                    }
                }

                File file2 = new File("/sdcard/E Libra/Library/setting-structure.json");
                if (file2.exists()) {
                    if (file2.delete()) {
                         Log.d("log_message","removed previous file library-structure.json");
                    }
                }


                try {
                    if (unzip(new File(editTextImportPath.getText().toString()), new File("/sdcard/E Libra/Library"))) {
                        nestedScrollViewStatusOutput.setVisibility(View.VISIBLE);
                        StringBuilder jsonData = new StringBuilder();
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader("/sdcard/E Libra/Library/library-structure.json"));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                jsonData.append(line);
                            }
                            reader.close();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "No Books Found in This Backup File", Toast.LENGTH_SHORT).show();
                        }

                        if(!jsonData.toString().isEmpty()) {
                            JSONArray importjsonArray = new JSONArray(jsonData.toString());
                            for (int i = 0; i < importjsonArray.length(); i++) {
                                JSONObject json = importjsonArray.getJSONObject(i);
                                JSONObject newJson = new JSONObject();
                                if (json.getString("image_path").isEmpty()) {
                                    newJson.put("image_path", json.getString("image_path"));
                                } else {
                                    newJson.put("image_path", "/sdcard/E Libra/Library/" + json.getString("name") + "/" + json.getString("image_path").substring(json.getString("image_path").lastIndexOf("/") + 1));
                                }
                                newJson.put("name", json.getString("name"));
                                if (json.getString("storage").isEmpty()) {
                                    newJson.put("storage", json.getString("storage"));
                                } else {
                                    newJson.put("storage", "/sdcard/E Libra/Library/" + json.getString("name") + "/" + json.getString("storage").substring(json.getString("storage").lastIndexOf("/") + 1));
                                }
                                newJson.put("page", json.getInt("page"));
                                newJson.put("total_pages", json.getInt("total_pages"));
                                added++;
                                libraryJsonArray.put(newJson);

                            }
                            migrationOutput("[ ANALYSED DATA ]", type);
                            migrationOutput("\n\n" + formatJson(importjsonArray), type);
                            migrationOutput("[ STATUS ]\n\nADDED - " + added + "\n" + "TOTAL -" + importjsonArray.length() + "\n", type);
                            save();
                        }

                        if(checkBoxImportSettings.isChecked()){
                            StringBuilder jsonData2 = new StringBuilder();
                            try {
                                BufferedReader reader2 = new BufferedReader(new FileReader("/sdcard/E Libra/Library/setting-structure.json"));
                                String line;
                                while ((line = reader2.readLine()) != null) {
                                    jsonData2.append(line);
                                }
                                reader2.close();
                            } catch (FileNotFoundException e) {
                                Toast.makeText(getApplicationContext(), "No Settings Configuration Found in This Backup File", Toast.LENGTH_SHORT).show();
                            }

                            settingJsonArray = new JSONArray(jsonData2.toString());

                            JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
                            text_selected = jsonObject0.getInt("selected");
                            textStyle = jsonObject0.getString("style");
                            autoCompleteTextViewStyle.setText(textStyle, false);
                            editTextSize.setText(String.valueOf(jsonObject0.getInt("size")));
                            JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
                            orientation_selected = jsonObject1.getInt("selected");
                            orientationValue = jsonObject1.getString("value");
                            JSONObject jsonObject2 = settingJsonArray.getJSONObject(2);
                            migration_selected = jsonObject2.getInt("selected");
                            if (text_selected == 0) {
                                constraintLayoutHiddenView.setVisibility(View.GONE);
                                imageViewTextToggle.setImageResource(android.R.drawable.arrow_down_float);
                            } else {
                                constraintLayoutHiddenView.setVisibility(View.VISIBLE);
                                imageViewTextToggle.setImageResource(android.R.drawable.arrow_up_float);
                            }
                            if (orientation_selected == 0) {
                                constraintLayoutOrientationHiddenView.setVisibility(View.GONE);
                                imageViewOrientationToggle.setImageResource(android.R.drawable.arrow_down_float);
                            } else {
                                constraintLayoutOrientationHiddenView.setVisibility(View.VISIBLE);
                                imageViewOrientationToggle.setImageResource(android.R.drawable.arrow_up_float);
                            }
                            if (migration_selected == 0) {
                                constraintLayoutMigrationHiddenView.setVisibility(View.GONE);
                                imageViewMigrationToggle.setImageResource(android.R.drawable.arrow_down_float);
                            } else {
                                constraintLayoutMigrationHiddenView.setVisibility(View.VISIBLE);
                                imageViewMigrationToggle.setImageResource(android.R.drawable.arrow_up_float);
                            }
                            if (orientationValue.equals("Sensor")) {
                                radioButtonSensor.setChecked(true);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                            }
                            if (orientationValue.equals("Portrait")) {
                                radioButtonPortrait.setChecked(true);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                            if (orientationValue.equals("Landscape")) {
                                radioButtonLandscape.setChecked(true);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                            }
                            if (remove_existing_data_status == 1) {
                                checkBoxRemoveExisting.setChecked(true);
                            } else {
                                checkBoxRemoveExisting.setChecked(false);
                            }
                            if (dont_import_status == 1) {
                                checkBoxImportSettings.setChecked(true);
                            } else {
                                checkBoxImportSettings.setChecked(false);
                            }
                            if (dont_export_status == 1) {
                                checkBoxExportSettings.setChecked(true);
                            } else {
                                checkBoxExportSettings.setChecked(false);
                            }
                            migrationOutput("[ SETTINGS DATA ]",type);
                            migrationOutput("\n\n"+formatJson(settingJsonArray),type);
                        }

                    } else {
                        Toast.makeText(this, "Path Not Found", Toast.LENGTH_SHORT).show();
                    }

                    } catch (Exception e) {
                        Toast.makeText(this, "error library json"+e, Toast.LENGTH_SHORT).show();
                        Log.d("error",e.toString());
                    }


            }
        });
        checkBoxRemoveExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxRemoveExisting.isChecked()) {
                    remove_existing_data_status=1;
                } else {
                    remove_existing_data_status=0;
                }
            }
        });
        checkBoxImportSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxImportSettings.isChecked()) {
                    dont_import_status=1;
                } else {
                    dont_import_status=0;
                }
            }
        });
        checkBoxExportSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxExportSettings.isChecked()) {
                    dont_export_status=1;
                } else {
                    dont_export_status=0;
                }
            }
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
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
                formattedJson.append("\t\t\t\t |---").append(key).append(" : ").append(value).append("\n");
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
            import_path = editTextExportPath.getText().toString();
            textViewOutputPreview.setText(import_output);
            buttonImport.setEnabled(false);
            buttonExport.setEnabled(true);
            textViewZip.setVisibility(View.GONE);
            editTextExportFile.setVisibility(View.GONE);
            editTextExportPath.setVisibility(View.GONE);
            imageViewFolderPicker.setVisibility(View.GONE);
            checkBoxRemoveExisting.setVisibility(View.VISIBLE);
            checkBoxImportSettings.setVisibility(View.VISIBLE);
            editTextImportPath.setVisibility(View.VISIBLE);
            imageViewFilePicker.setVisibility(View.VISIBLE);
            checkBoxExportSettings.setVisibility(View.GONE);
        }
        if(type.equals("export")){
            export_path = editTextImportPath.getText().toString();
            textViewOutputPreview.setText(export_output);
            buttonExport.setEnabled(false);
            buttonImport.setEnabled(true);
            textViewZip.setVisibility(View.VISIBLE);
            editTextExportFile.setVisibility(View.VISIBLE);
            editTextExportPath.setVisibility(View.VISIBLE);
            imageViewFolderPicker.setVisibility(View.VISIBLE);
            checkBoxRemoveExisting.setVisibility(View.GONE);
            checkBoxImportSettings.setVisibility(View.GONE);
            editTextImportPath.setVisibility(View.GONE);
            imageViewFilePicker.setVisibility(View.GONE);
            checkBoxExportSettings.setVisibility(View.VISIBLE);
        }
    }
    public void migrationOutput(String string,String type){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(type.equals("import")){
            import_output+=string;
            textViewOutputPreview.setText(import_output);
        }
        if(type.equals("export")){
            export_output+=string;
            textViewOutputPreview.setText(export_output);
        }
        nestedScrollViewStatusOutput.scrollTo(0, textViewOutputPreview.getBottom());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        item1 = menu.findItem(R.id.item1);
        item2 = menu.findItem(R.id.item2);
        item3 = menu.findItem(R.id.item3);
        setMenuItemStyle(item1, textStyle, Integer.parseInt(editTextSize.getText().toString()));
        setMenuItemStyle(item2, textStyle, Integer.parseInt(editTextSize.getText().toString()));
        setMenuItemStyle(item3, textStyle, Integer.parseInt(editTextSize.getText().toString()));
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
        textViewExit = dialogView.findViewById(R.id.textViewExit);
        textViewExitMessage = dialogView.findViewById(R.id.textViewExitMessage);
        buttonExitNo = dialogView.findViewById(R.id.buttonExitNo);
        buttonExitYes = dialogView.findViewById(R.id.buttonExitYes);
        exit_text_change();
        alert.setView(dialogView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        buttonExitNo.setOnClickListener(v1 -> alertDialog.dismiss());
        buttonExitYes.setOnClickListener(v12 -> {
            finishAffinity();
        });
        alertDialog.show();
    }

    public void file_replace_dialog(File folderToZip, File zippedFile){
        final AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
        fileReplaceDialog = getLayoutInflater().inflate(R.layout.file_replace_dialog, null);
        textViewFileReplace = fileReplaceDialog.findViewById(R.id.textViewFileReplace);
        textViewFileReplaceMessage = fileReplaceDialog.findViewById(R.id.textViewFileReplaceMessage);
        buttonFileReplaceNo = fileReplaceDialog.findViewById(R.id.buttonFileReplaceNo);
        buttonFileReplaceYes = fileReplaceDialog.findViewById(R.id.buttonFileReplaceYes);
        file_replace_text_change();
        alert.setView(fileReplaceDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        buttonFileReplaceNo.setOnClickListener(v1 -> {
            migrationOutput("[ STATUS ]-( CANCELLED )\n\n[ TARGETED PATH ] - "+zippedFile+"\n\n",type);
            alertDialog.dismiss();
        });
        buttonFileReplaceYes.setOnClickListener(v12 -> {
            try {
                Toast.makeText(this, "zip function running", Toast.LENGTH_SHORT).show();
                zip(folderToZip, zippedFile);
                migrationOutput("[ STATUS ]-( REPLACED )\n\n[ PATH ] - "+zippedFile+"\n\n",type);
                deleteFolder(folderToZip);
                alertDialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "error while files zipping", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    public void file_replace_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewFileReplace.setTypeface(typeface);
        textViewFileReplaceMessage.setTypeface(typeface);
        buttonFileReplaceNo.setTypeface(typeface);
        buttonFileReplaceYes.setTypeface(typeface);
        textViewFileReplace.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewFileReplaceMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonFileReplaceNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonFileReplaceYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }
    public void settings_text_change(){
        SpannableString spanString1 = null;
        SpannableString spanString2 = null;
        SpannableString spanString3 = null;
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewStyle.setTypeface(typeface);
        textViewAdjustSize.setTypeface(typeface);
        buttonImport.setTypeface(typeface);
        textViewSettings.setTypeface(typeface);
        textViewText.setTypeface(typeface);
        textViewOrientation.setTypeface(typeface);
        textViewMigration.setTypeface(typeface);
        autoCompleteTextViewStyle.setTypeface(typeface);
        buttonSizeIncrement.setTypeface(typeface);
        buttonSizeDecrement.setTypeface(typeface);
        buttonProceed.setTypeface(typeface);
        radioButtonSensor.setTypeface(typeface);
        radioButtonPortrait.setTypeface(typeface);
        radioButtonLandscape.setTypeface(typeface);
        buttonExport.setTypeface(typeface);
        checkBoxRemoveExisting.setTypeface(typeface);
        checkBoxImportSettings.setTypeface(typeface);
        editTextSize.setTypeface(typeface);
        editTextExportPath.setTypeface(typeface);
        editTextExportFile.setTypeface(typeface);
        textViewZip.setTypeface(typeface);
        checkBoxExportSettings.setTypeface(typeface);
        editTextImportPath.setTypeface(typeface);
        textViewOutputPreview.setTypeface(typeface);
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
            spanString1.setSpan(new TypefaceSpan(textStyle), 0, spanString1.length(), 0);
        }
        if (spanString2 != null) {
            spanString2.setSpan(new TypefaceSpan(textStyle), 0, spanString2.length(), 0);
        }
        if (spanString3 != null) {
            spanString3.setSpan(new TypefaceSpan(textStyle), 0, spanString3.length(), 0);
        }
        int newTextSize = Integer.parseInt(editTextSize.getText().toString());
        if(newTextSize>=10) {
            textViewStyle.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewAdjustSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonImport.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewSettings.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewOrientation.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewMigration.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            autoCompleteTextViewStyle.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonSizeIncrement.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonSizeDecrement.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonProceed.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewText.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            radioButtonSensor.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            radioButtonPortrait.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            radioButtonLandscape.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonExport.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            checkBoxRemoveExisting.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            checkBoxImportSettings.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextExportPath.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextExportFile.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewZip.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            checkBoxExportSettings.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            editTextImportPath.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewOutputPreview.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
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
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewExit.setTypeface(typeface);
        textViewExitMessage.setTypeface(typeface);
        buttonExitNo.setTypeface(typeface);
        buttonExitYes.setTypeface(typeface);
        int newTextSize = Integer.parseInt(editTextSize.getText().toString());
        if(newTextSize>=10) {
            textViewExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            textViewExitMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonExitNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
            buttonExitYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
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
                    editTextImportPath.setText(filePath);
                }
                if(type.equals("export")){
                    editTextExportPath.setText(filePath);
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
        boolean hasRequiredFiles = false;
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
                ZipEntry entry = zis.getNextEntry();
                while (entry != null) {
                    String fileName = entry.getName();
                    if (fileName.equals("library-structure.json") || fileName.equals("setting-structure.json")) {
                        hasRequiredFiles = true;
                        break;
                    }
                    entry = zis.getNextEntry();
                }
        } catch (IOException e) {
            Log.d("log_message","Error reading zip file: " + e.getMessage());
            return false;
        }
        if (!hasRequiredFiles) {
            Toast.makeText(this, "Zip file does not contain the required files.", Toast.LENGTH_SHORT).show();
            return false;
        }
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
                        Log.d("error",e.getMessage());
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
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
       try {
           settingJsonArray = new JSONArray(prefs.getString("settingJsonArray", "[]"));
           JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
            text_selected = jsonObject0.getInt("selected");
            textStyle =jsonObject0.getString("style");
            autoCompleteTextViewStyle.setText(textStyle, false);
            editTextSize.setText(String.valueOf(jsonObject0.getInt("size")));
            JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
            orientation_selected = jsonObject1.getInt("selected");
            orientationValue = jsonObject1.getString("value");
            JSONObject jsonObject2 = settingJsonArray.getJSONObject(2);
            migration_selected = jsonObject2.getInt("selected");
            type = jsonObject2.getString("type");
            editTextExportFile.setText(jsonObject2.getString("export_name"));
            export_path = jsonObject2.getString("export_path");
            editTextExportPath.setText(export_path);
            dont_export_status = jsonObject2.getInt("export_setting_status");
            import_path = jsonObject2.getString("import_path");
            editTextImportPath.setText(import_path);
            dont_import_status = jsonObject2.getInt("import_setting_status");
            remove_existing_data_status = jsonObject2.getInt("remove_existing_data_status");
            if(text_selected==0){
                constraintLayoutHiddenView.setVisibility(View.GONE);
                imageViewTextToggle.setImageResource(android.R.drawable.arrow_down_float);
            }else{
                constraintLayoutHiddenView.setVisibility(View.VISIBLE);
                imageViewTextToggle.setImageResource(android.R.drawable.arrow_up_float);
            }
            if(orientation_selected==0){
                constraintLayoutOrientationHiddenView.setVisibility(View.GONE);
                imageViewOrientationToggle.setImageResource(android.R.drawable.arrow_down_float);
            }else{
                constraintLayoutOrientationHiddenView.setVisibility(View.VISIBLE);
                imageViewOrientationToggle.setImageResource(android.R.drawable.arrow_up_float);
            }
            if (migration_selected==0) {
                constraintLayoutMigrationHiddenView.setVisibility(View.GONE);
                imageViewMigrationToggle.setImageResource(android.R.drawable.arrow_down_float);
            }else {
                constraintLayoutMigrationHiddenView.setVisibility(View.VISIBLE);
                imageViewMigrationToggle.setImageResource(android.R.drawable.arrow_up_float);
            }
            if(orientationValue.equals("Sensor")){
                radioButtonSensor.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            if(orientationValue.equals("Portrait")){
                radioButtonPortrait.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if(orientationValue.equals("Landscape")){
                radioButtonLandscape.setChecked(true);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
            if(remove_existing_data_status==1){
                checkBoxRemoveExisting.setChecked(true);
            }else{
                checkBoxRemoveExisting.setChecked(false);
            }
            if(dont_import_status==1){
                checkBoxImportSettings.setChecked(true);
            }else{
                checkBoxImportSettings.setChecked(false);
            }
           if(dont_export_status==1){
               checkBoxExportSettings.setChecked(true);
           }else{
               checkBoxExportSettings.setChecked(false);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }


    private void save_setting() {
        settingJsonArray = new JSONArray();
        int textSize = Integer.parseInt(editTextSize.getText().toString());
        if (textSize < 10) {
            textSize = 40;
        }
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("selected", text_selected);
            jsonObject1.put("size", textSize);
            jsonObject1.put("style", textStyle);
            settingJsonArray.put(0, jsonObject1);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("selected", orientation_selected);
            jsonObject2.put("value", orientationValue);
            settingJsonArray.put(1, jsonObject2);
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("selected", migration_selected);
            jsonObject3.put("type", type);
            jsonObject3.put("export_name", editTextExportFile.getText().toString());
            jsonObject3.put("export_path", editTextExportPath.getText().toString());
            jsonObject3.put("export_setting_status", dont_export_status);
            jsonObject3.put("import_path", editTextImportPath.getText().toString());
            jsonObject3.put("import_setting_status", dont_import_status);
            jsonObject3.put("remove_existing_data_status", remove_existing_data_status);
            settingJsonArray.put(2, jsonObject3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("settingJsonArray", settingJsonArray.toString());
        editor.apply();
    }
    private void save() {
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("libraryJsonArray", libraryJsonArray.toString());
        editor.apply();
    }
}


