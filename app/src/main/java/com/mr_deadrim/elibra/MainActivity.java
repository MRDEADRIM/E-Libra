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
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
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
import java.util.HashSet;
import java.util.Set;
public class MainActivity extends AppCompatActivity {

    private JSONArray libraryJsonArray,settingJsonArray;
    private View dialogView;
    public EditText textViewBookName, textViewBookPath;
    private static final int PICK_FILE_REQUEST_CODE = 1, PICK_IMAGE_REQUEST_CODE =21;
    RecyclerAdapter recyclerAdapter;
    boolean isAllFieldsChecked = false;
    String image_path="", textStyle ="sans-serif", orientationValue ="Sensor";
    int textSize =40;
    Button buttonButtonCancel,buttonButtonSave;
    ImageView imageButtonBookIcon,textViewBookPick, buttonButtonIconEdit;
    TextView textViewExit,textViewExitMessage,textViewBookIconLabel, textViewBookNameLabel;

    Button buttonAddBook,buttonExitNo,buttonExitYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        buttonAddBook = findViewById(R.id.buttonAddBook);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBookList);

        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        try {
            libraryJsonArray = new JSONArray(prefs.getString("libraryJsonArray", "[]"));
            settingJsonArray = new JSONArray(prefs.getString("settingJsonArray", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        load();
        buttonAddBook.setOnClickListener(v -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            dialogView = getLayoutInflater().inflate(R.layout.dialog, null);
            textViewBookIconLabel = dialogView.findViewById(R.id.textViewBookIconLabel);
            textViewBookNameLabel = dialogView.findViewById(R.id.textViewBookNameLabel);
            textViewBookName = dialogView.findViewById(R.id.textViewBookName);
            textViewBookPath = dialogView.findViewById(R.id.textViewBookPath);
            buttonButtonCancel = dialogView.findViewById(R.id.buttonBookCancel);
            buttonButtonSave = dialogView.findViewById(R.id.buttonBookSave);
            textViewBookPick = dialogView.findViewById(R.id.imageViewBookPick);
            buttonButtonIconEdit = dialogView.findViewById(R.id.imageViewBookIconPick);
            imageButtonBookIcon = dialogView.findViewById(R.id.imageButtonBookIcon);

            if(image_path.isEmpty()){
                imageButtonBookIcon.setImageResource(R.mipmap.ic_launcher);
            }

            textViewBookPick.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
                intent.putExtra("status", "add_document");
                ((Activity) MainActivity.this).startActivityForResult(intent, PICK_FILE_REQUEST_CODE);

            });

            buttonButtonIconEdit.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
                intent.putExtra("status", "add_image");
                ((Activity) MainActivity.this).startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
            });

            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);
            buttonButtonCancel.setOnClickListener(v1 -> alertDialog.dismiss());
            buttonButtonSave.setOnClickListener(v12 -> {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    try {
                        JSONObject json = new JSONObject();
                        json.put("image_path",image_path);
                        json.put("name", textViewBookName.getText().toString());
                        json.put("storage", textViewBookPath.getText().toString());
                        json.put("page",0);
                        json.put("total_pages",0);
                        libraryJsonArray.put(json);
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

        recyclerAdapter = new RecyclerAdapter(libraryJsonArray, settingJsonArray);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = getItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private boolean CheckAllFields() {
        String bookName = textViewBookName.getText().toString().trim();
        String bookPath = textViewBookPath.getText().toString().trim();
        if (bookName.isEmpty()) {
            textViewBookName.setError("This field is required");
            return false;
        }
        Set<String> existingNames = new HashSet<>();
        for (int i = 0; i < libraryJsonArray.length(); i++) {
            try {
                JSONObject json = libraryJsonArray.getJSONObject(i);
                existingNames.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (existingNames.contains(bookName)) {
            textViewBookName.setError("This name already exists");
            return false;
        }
        if (bookPath.isEmpty()) {
            textViewBookPath.setError("This field is required");
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
                    JSONObject temp = libraryJsonArray.getJSONObject(fromPosition);
                    libraryJsonArray.put(fromPosition, libraryJsonArray.get(toPosition));
                    libraryJsonArray.put(toPosition, temp);
                    recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                    save();
                } catch (JSONException e) {
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
                textViewBookPath.setText(filePath);
            }
            if(requestCode == 2) {
                recyclerAdapter.setFilePath(filePath);
            }
            if (requestCode == 21) {
                imageButtonBookIcon.setImageURI(Uri.parse(filePath));
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
        setMenuItemStyle(item1, textStyle, textSize);
        setMenuItemStyle(item2, textStyle, textSize);
        setMenuItemStyle(item3, textStyle, textSize);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        exit();
        return true;
    }

    public void exit(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
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
            alertDialog.dismiss();
            finishAffinity();
        });
        alertDialog.show();
    }

    private void save() {
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("libraryJsonArray", libraryJsonArray.toString());
        editor.apply();
    }

    private void load() {
        try {
            if (settingJsonArray.length() > 1) {
                JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
                textStyle = jsonObject0.getString("style");
                textSize = jsonObject0.getInt("size");
                JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
                orientationValue = jsonObject1.getString("value");
                if (orientationValue.equals("Sensor")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                if (orientationValue.equals("Portrait")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                if (orientationValue.equals("Landscape")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
                main_text_change();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dialog_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewBookName.setTypeface(typeface);
        textViewBookPath.setTypeface(typeface);
        buttonButtonCancel.setTypeface(typeface);
        buttonButtonSave.setTypeface(typeface);
        textViewBookIconLabel.setTypeface(typeface);
        textViewBookNameLabel.setTypeface(typeface);
        textViewBookName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewBookPath.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonButtonCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonButtonSave.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewBookIconLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewBookNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    public void exit_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        textViewExit.setTypeface(typeface);
        textViewExitMessage.setTypeface(typeface);
        buttonExitNo.setTypeface(typeface);
        buttonExitYes.setTypeface(typeface);
        textViewExit.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewExitMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonExitNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonExitYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void main_text_change(){
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        buttonAddBook.setTypeface(typeface);
        buttonAddBook.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}