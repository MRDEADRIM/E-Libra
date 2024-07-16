package com.mr_deadrim.elibra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PdfActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PdfRenderer pdfRenderer;
    public String storage;
    public int position, current_page = 1, total_pages;
    private Toast toast;
    private LinearLayoutManager layoutManager;
    private PdfAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        storage = intent.getStringExtra("storage");
        position = intent.getIntExtra("position", 0);
        current_page = intent.getIntExtra("page", 0);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        try {
            openPdfRenderer();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF file", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new PdfAdapter();
        recyclerView.setAdapter(adapter);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    current_page = firstVisibleItemPosition + 1;
                    updateToast();
                    Log.d("page_count", String.valueOf(current_page));
                }
            }
        });

        scrollToPage(current_page);
    }

    private void openPdfRenderer() throws IOException {
        File file = new File(storage);
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        total_pages = pdfRenderer.getPageCount();
    }

    private class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

        @NonNull
        @Override
        public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
            return new PdfViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return total_pages;
        }

        class PdfViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            PdfViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }

            void bind(int position) {
                if (pdfRenderer != null) {
                    PdfRenderer.Page page = pdfRenderer.openPage(position);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    imageView.setImageBitmap(bitmap);
                    page.close();
                }
            }
        }
    }

    private void scrollToPage(int page) {
        if (page > 0 && page <= total_pages) {
            recyclerView.scrollToPosition(page - 1);
        }
    }

    private void updateToast() {
        if (toast != null) {
            toast.setText(" [ " + current_page + " | " + total_pages + " ] ");
            toast.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
    }

    private void savePage() {
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString("key", "[]"));
            JSONObject json = jsonArray.getJSONObject(position);
            json.put("page", current_page);
            json.put("total_pages", total_pages);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("key", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toast.setText("[ Progress Saved ]");
        toast.show();
    }
}
