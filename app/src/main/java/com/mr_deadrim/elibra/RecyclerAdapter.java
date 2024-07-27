package com.mr_deadrim.elibra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static JSONArray jsonArray;
    private static JSONArray json2Array;
    public EditText nameEditText,storageEditText;
    TextView textView3,textView2;
    public ImageView imagePreview;
    public View dialogView;
    public String image_path,style="sans-serif";
    int size=40;

    public RecyclerAdapter(JSONArray jsonArray,JSONArray json2Array) {
        this.jsonArray = jsonArray;
        this.json2Array = json2Array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    public void setFilePath(String filePath) {
        storageEditText.setText(filePath);
    }
    public void setImagePath(String filePath){
        if(!filePath.isEmpty()){
            imagePreview.setImageURI(Uri.parse(filePath));
            image_path=filePath;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String name = jsonObject.getString("name");
            String page = jsonObject.getString("page");
            String total_pages = jsonObject.getString("total_pages");
            String image_path = jsonObject.getString("image_path");
            if(image_path.isEmpty()){
                holder.imageView.setImageResource(R.mipmap.ic_launcher);
            }else{
                holder.imageView.setImageURI(Uri.parse(image_path));
            }
            holder.name.setText(name);
            holder.pages.setText(" [ " + page + " | " + total_pages +" ] ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        private ImageView imageView;
        private TextView name,pages;
        private ImageView updateButton, deleteButton;
        private String storage;
        private Button cancelButton;
        private Button saveButton;
        private static final int PICK_FILE_REQUEST_CODE = 2,PICK_IMAGE_REQUEST=22;

        boolean isAllFieldsChecked = false;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textViewName);
            pages = itemView.findViewById(R.id.textViewPages);
            updateButton = itemView.findViewById(R.id.btn_update);
            deleteButton = itemView.findViewById(R.id.btn_delete);


            try{
                JSONObject jsonObject0 = json2Array.getJSONObject(0);
                style=jsonObject0.getString("style");
                size= jsonObject0.getInt("size");
            }catch(Exception e){
                Toast.makeText(itemView.getContext(), "error in retreating json2array", Toast.LENGTH_SHORT).show();
            }

            Handler handler = new Handler();
            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(v -> {
                updateButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                handler.postDelayed(() -> {
                    updateButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                }, 5000);

                return true;
            });

            updateButton.setOnClickListener(v -> {

                Context context = itemView.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                dialogView = inflater.inflate(R.layout.dialog, null);
                nameEditText = dialogView.findViewById(R.id.name);
                storageEditText = dialogView.findViewById(R.id.storage);
                imagePreview = dialogView.findViewById(R.id.imageButton);
                textView3 = dialogView.findViewById(R.id.textView3);
                textView2 = dialogView.findViewById(R.id.textView2);
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());

                    String name = jsonObject.getString("name");
                    storage = jsonObject.getString("storage");
                    String image_path=jsonObject.getString("image_path");
                    storageEditText.setText(storage);
                    nameEditText.setText(name);

                    if(image_path.isEmpty()){
                        imagePreview.setImageResource(R.mipmap.ic_launcher);
                    }else{
                        imagePreview.setImageURI(Uri.parse(image_path));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                cancelButton = dialogView.findViewById(R.id.btn_cancel);
                saveButton = dialogView.findViewById(R.id.btn_okay);
                ImageView fileManager = dialogView.findViewById(R.id.button);
                ImageView imageButton =dialogView.findViewById(R.id.button3);

                fileManager.setOnClickListener(view -> {
                    Intent intent = new Intent(context, FileManagerActivity.class);
                    intent.putExtra("status", "add_document");
                    ((Activity) context).startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
                });

                imageButton.setOnClickListener(view -> {
                    Intent intent = new Intent(context, FileManagerActivity.class);
                    intent.putExtra("status", "add_image");
                    ((Activity) context).startActivityForResult(intent, PICK_IMAGE_REQUEST);
                });
                dialog_text_change();
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                saveButton.setOnClickListener(v1 -> {
                    isAllFieldsChecked = CheckAllFields();
                    if(isAllFieldsChecked) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());
                            String newName = nameEditText.getText().toString();
                            String newStorage = storageEditText.getText().toString();
                            jsonObject.put("name", newName);
                            jsonObject.put("storage", newStorage);
                            if (image_path != null) {
                                jsonObject.put("image_path", image_path);
                            }
                            if(!storage.equals(storageEditText.getText().toString())){
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
                
                cancelButton.setOnClickListener(v12 -> dialog.dismiss());
            });

            deleteButton.setOnClickListener(v -> {
                jsonArray.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                save();
            });

            main_text_change();
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

        @Override
        public void onClick(View v) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());
                String storagePath = jsonObject.getString("storage");
                Integer page = jsonObject.getInt("page");
                File file = new File(storagePath);
                if (file.exists()) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, PdfActivity.class);
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

        public void dialog_text_change(){
            Typeface typeface = Typeface.create(style, Typeface.NORMAL);
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

        public void main_text_change(){
            Typeface typeface = Typeface.create(style, Typeface.NORMAL);
            name.setTypeface(typeface);
            pages.setTypeface(typeface);
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            pages.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
        private void save() {
            SharedPreferences prefs = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("key", jsonArray.toString());
            editor.apply();
            Log.d("array_data", "save array data: " + jsonArray.toString());
        }
    }
}
