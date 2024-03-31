package com.mr_deadrim.ebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
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
    public EditText nameEditText,storageEditText;
    public ImageView imagePreview;
    public View dialogView;
    public File imgFile;
    Bitmap bitmap;

    public String image_path;

    public RecyclerAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
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
        File imgFile = new  File(filePath);
        if(imgFile.exists()) {
            bitmap= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imagePreview.setImageBitmap(bitmap);
        }else{

        }
        image_path=filePath;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String name = jsonObject.getString("name");
            String page = jsonObject.getString("page");
            String total_pages = jsonObject.getString("total_pages");
            String image_path = jsonObject.getString("image_path");
            File imgFile = new  File(image_path);
            if(imgFile.exists()) {
                bitmap= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            }else{
            }
            holder.name.setText(name);
            holder.pages.setText(" [ " + page + " | " + total_pages + " ] ");
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
        private TextView name,pages,rowCountTextView;
        private Button updateButton, deleteButton;
        private static final int PICK_FILE_REQUEST_CODE = 2,PICK_IMAGE_REQUEST=22;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textViewName);
            pages = itemView.findViewById(R.id.textViewPages);
            updateButton = itemView.findViewById(R.id.btn_update);
            deleteButton = itemView.findViewById(R.id.btn_delete);

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
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());
                    String name = jsonObject.getString("name");
                    String storage = jsonObject.getString("storage");
                    storageEditText.setText(storage);
                    nameEditText.setText(name);

                    File imgFile = new  File(jsonObject.getString("image_path"));
                    if(imgFile.exists()) {
                        bitmap= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imagePreview.setImageBitmap(bitmap);
                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
                Button saveButton = dialogView.findViewById(R.id.btn_okay);
                Button fileManager = dialogView.findViewById(R.id.button);
                Button imageButton =dialogView.findViewById(R.id.button3);


                fileManager.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("application/pdf");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    ((Activity) context).startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
                });

                imageButton.setOnClickListener(view -> {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) context).startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                });

                builder.setView(dialogView);
                builder.setTitle("Update");
                AlertDialog dialog = builder.create();
                dialog.show();

                saveButton.setOnClickListener(v1 -> {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());
                        String newName = nameEditText.getText().toString();
                        String newStorage = storageEditText.getText().toString();
                        jsonObject.put("name", newName);
                        jsonObject.put("storage", newStorage);
                        jsonObject.put("image_path", image_path);
                        notifyItemChanged(getAdapterPosition());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    save();
                    dialog.dismiss();
                });

                cancelButton.setOnClickListener(v12 -> dialog.dismiss());
            });

            deleteButton.setOnClickListener(v -> {
                jsonArray.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                save();
            });
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

        private void save() {
            SharedPreferences prefs = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("key", jsonArray.toString());
            editor.apply();
            Log.d("array_data", "save array data: " + jsonArray.toString());
        }
    }
}
