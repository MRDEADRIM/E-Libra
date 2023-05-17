package com.mr_deadrim.ebook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static JSONArray jsonArray;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String name = jsonObject.getString("name");
            String storage = jsonObject.getString("storage");
            holder.textView.setText(String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView, rowCountTextView;
        private Button updateButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);
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
                View dialogView = inflater.inflate(R.layout.dialog, null);
                EditText nameEditText = dialogView.findViewById(R.id.name);
                EditText storageEditText = dialogView.findViewById(R.id.storage);

                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(getAdapterPosition());
                    String name = jsonObject.getString("name");
                    String storage = jsonObject.getString("storage");
                    storageEditText.setText(storage);
                    nameEditText.setText(name);
                    Toast.makeText(context, "storage" + storage, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
                Button saveButton = dialogView.findViewById(R.id.btn_okay);

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
                Context context = itemView.getContext();
                PdfActivity.startActivity(context, jsonObject.toString());
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