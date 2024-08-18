package com.mr_deadrim.elibra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static JSONArray libraryJsonArray,settingJsonArray;
    public EditText textViewBookName, textViewBookPath;
    TextView textViewBookIconLabel, textViewBookNameLabel;
    public ImageView imageButtonBookIcon;
    public View dialogView;
    public String image_path, textStyle ="sans-serif", orientationValue;
    int textSize =30;
    public RecyclerAdapter(JSONArray liberaryjsonArray,JSONArray settingJsonArray) {
        this.libraryJsonArray = liberaryjsonArray;
        this.settingJsonArray = settingJsonArray;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }
    public void setFilePath(String filePath) {
        textViewBookPath.setText(filePath);
    }
    public void setImagePath(String filePath){
        if(!filePath.isEmpty()){
            imageButtonBookIcon.setImageURI(Uri.parse(filePath));
            image_path=filePath;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = libraryJsonArray.getJSONObject(position);
            String name = jsonObject.getString("name");
            String page = jsonObject.getString("page");
            String total_pages = jsonObject.getString("total_pages");
            String image_path = jsonObject.getString("image_path");
            if(image_path.isEmpty()){
                holder.imageViewAppIcon.setImageResource(R.mipmap.ic_launcher);
            }else{
                holder.imageViewAppIcon.setImageURI(Uri.parse(image_path));
            }
            holder.textViewName.setText(name);
            holder.textViewPages.setText(" [ " + page + " | " + total_pages +" ] ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return libraryJsonArray.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        private TextView textViewName, textViewPages;
        private ImageView imageViewAppIcon, imageViewUpdate, imageViewDelete;
        private String storage;
        private Button buttonBookCancel,buttonBookSave;
        private static final int PICK_FILE_REQUEST_CODE = 2,PICK_IMAGE_REQUEST=22;
        boolean isAllFieldsChecked = false;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAppIcon = itemView.findViewById(R.id.imageViewAppIcon);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPages = itemView.findViewById(R.id.textViewPages);
            imageViewUpdate = itemView.findViewById(R.id.imageViewUpdate);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            try{
                JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
                textStyle = jsonObject0.getString("style");
                textSize = jsonObject0.getInt("size");
                JSONObject jsonObject1 = settingJsonArray.getJSONObject(1);
                orientationValue = jsonObject1.getString("value");
            }catch(Exception e){
                Toast.makeText(itemView.getContext(), "error in retreating json2array", Toast.LENGTH_SHORT).show();
            }
            Handler handler = new Handler();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(v -> {
                imageViewUpdate.setVisibility(View.VISIBLE);
                imageViewDelete.setVisibility(View.VISIBLE);
                handler.postDelayed(() -> {
                    imageViewUpdate.setVisibility(View.GONE);
                    imageViewDelete.setVisibility(View.GONE);
                }, 5000);

                return true;
            });
            imageViewUpdate.setOnClickListener(v -> {
                Context context = itemView.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                dialogView = inflater.inflate(R.layout.dialog, null);
                textViewBookName = dialogView.findViewById(R.id.textViewBookName);
                textViewBookPath = dialogView.findViewById(R.id.textViewBookPath);
                imageButtonBookIcon = dialogView.findViewById(R.id.imageButtonBookIcon);
                textViewBookIconLabel = dialogView.findViewById(R.id.textViewBookIconLabel);
                textViewBookNameLabel = dialogView.findViewById(R.id.textViewBookNameLabel);
                try {
                    JSONObject jsonObject = libraryJsonArray.getJSONObject(getAdapterPosition());
                    String name = jsonObject.getString("name");
                    storage = jsonObject.getString("storage");
                    String image_path=jsonObject.getString("image_path");
                    textViewBookPath.setText(storage);
                    textViewBookName.setText(name);
                    if(image_path.isEmpty()){
                        imageButtonBookIcon.setImageResource(R.mipmap.ic_launcher);
                    }else{
                        imageButtonBookIcon.setImageURI(Uri.parse(image_path));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                buttonBookCancel = dialogView.findViewById(R.id.buttonBookCancel);
                buttonBookSave = dialogView.findViewById(R.id.buttonBookSave);
                ImageView imageViewBookPick = dialogView.findViewById(R.id.imageViewBookPick);
                ImageView imageViewBookIconPick =dialogView.findViewById(R.id.imageViewBookIconPick);
                imageViewBookPick.setOnClickListener(view -> {
                    Intent intent = new Intent(context, FileManagerActivity.class);
                    intent.putExtra("status", "add_document");
                    ((Activity) context).startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
                });
                imageViewBookIconPick.setOnClickListener(view -> {
                    Intent intent = new Intent(context, FileManagerActivity.class);
                    intent.putExtra("status", "add_image");
                    ((Activity) context).startActivityForResult(intent, PICK_IMAGE_REQUEST);
                });
                dialog_text_change();
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
                buttonBookSave.setOnClickListener(v1 -> {
                    isAllFieldsChecked = CheckAllFields();
                    if(isAllFieldsChecked) {
                        try {
                            JSONObject jsonObject = libraryJsonArray.getJSONObject(getAdapterPosition());
                            String newName = textViewBookName.getText().toString();
                            String newStorage = textViewBookPath.getText().toString();
                            jsonObject.put("name", newName);
                            jsonObject.put("storage", newStorage);
                            if (image_path != null) {
                                jsonObject.put("image_path", image_path);
                            }
                            if(!storage.equals(textViewBookPath.getText().toString())){
                                jsonObject.put("page", 0);
                                jsonObject.put("total_pages",0);
                            }
                            notifyItemChanged(getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        save();
                        dialog.dismiss();
                        image_path = null;
                    }
                });
                buttonBookCancel.setOnClickListener(v12 -> dialog.dismiss());
            });
            imageViewDelete.setOnClickListener(v -> {
                JSONObject jsonObject = (JSONObject) libraryJsonArray.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                save();
                delete(jsonObject);
            });
            main_text_change();
        }
        private boolean CheckAllFields() {
            if (textViewBookName.length() == 0) {
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
            if (existingNames.contains(textViewName)) {
                textViewBookName.setError("This name already exists");
                return false;
            }
            if (textViewBookPath.length() == 0) {
                textViewBookPath.setError("This field is required");
                return false;
            }
            return true;
        }
        @Override
        public void onClick(View v) {
            try {
                JSONObject jsonObject = libraryJsonArray.getJSONObject(getAdapterPosition());
                String storagePath = jsonObject.getString("storage");
                Integer page = jsonObject.getInt("page");
                File file = new File(storagePath);
                if (file.exists()) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, PdfActivity.class);
                    intent.putExtra("orientation", orientationValue);
                    intent.putExtra("position",getAdapterPosition());
                    intent.putExtra("storage", storagePath);
                    intent.putExtra("page",page);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(itemView.getContext(), "File not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        public boolean deleteDirectory(File directory) {
            if (!directory.exists()) {
                Toast.makeText(itemView.getContext(), "Folder not found", Toast.LENGTH_SHORT).show();
                return false;
            }
            Set<String> existingStorage = new HashSet<>();
            for (int i = 0; i < libraryJsonArray.length(); i++) {
                try {
                    JSONObject json = libraryJsonArray.getJSONObject(i);
                    existingStorage.add(json.getString("storage"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (existingStorage.contains(storage)) {
                Toast.makeText(itemView.getContext(), " path unique found", Toast.LENGTH_SHORT).show();
            } else {
                if (directory.isDirectory()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (!deleteDirectory(file)) {
                                return false;
                            }
                        }
                    }

                }
            }
            return directory.delete();
        }
        private boolean isInsideDirectory(File file, File baseDirectory) {
            try {
                String filePath = file.getCanonicalPath();
                String baseDirPath = baseDirectory.getCanonicalPath();
                return filePath.startsWith(baseDirPath);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        public void dialog_text_change(){
            Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
            textViewBookName.setTypeface(typeface);
            textViewBookPath.setTypeface(typeface);
            buttonBookCancel.setTypeface(typeface);
            buttonBookSave.setTypeface(typeface);
            textViewBookIconLabel.setTypeface(typeface);
            textViewBookNameLabel.setTypeface(typeface);
            textViewBookName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textViewBookPath.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            buttonBookCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            buttonBookSave.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textViewBookIconLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textViewBookNameLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        public void main_text_change(){
            Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
            textViewName.setTypeface(typeface);
            textViewPages.setTypeface(typeface);
            textViewName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textViewPages.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        private void delete(JSONObject jsonObject){
            String storage = jsonObject.optString("storage");
            String image_path = jsonObject.optString("image_path");
            File file = !storage.isEmpty() ? new File(storage) : new File(image_path);
            File parentDir = file.getParentFile();
            File baseDirectory = new File("/sdcard/E Libra/Library/");
            if (isInsideDirectory(file, baseDirectory)) {
                if (parentDir != null && parentDir.exists()) {
                    boolean success = deleteDirectory(parentDir);
                    if (success) {
                        Toast.makeText(itemView.getContext(), "Deleting folder ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Failed to delete directory.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(itemView.getContext(), "Parent directory does not exist.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(itemView.getContext(), "Outside path", Toast.LENGTH_SHORT).show();
            }
        }
        private void save() {
            SharedPreferences prefs = itemView.getContext().getSharedPreferences("ShredPreferenceJsonData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("libraryJsonArray", libraryJsonArray.toString());
            editor.apply();
        }
    }
}
