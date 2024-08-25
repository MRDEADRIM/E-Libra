package com.mr_deadrim.elibra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class PdfActivity extends AppCompatActivity {

    private RecyclerView pdfLayout;
    private PdfRenderer pdfRenderer;
    public String storage,orientation;
    public int position, current_page = 1, total_pages;
    private Toast toast;
    private LinearLayoutManager layoutManager;
    private PdfAdapter pdfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Intent intent = getIntent();
        orientation = intent.getStringExtra("orientation");
        storage = intent.getStringExtra("storage");
        position = intent.getIntExtra("position", 0);
        current_page = intent.getIntExtra("page", 0);
        if(orientation.equals("Sensor")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        if(orientation.equals("Portrait")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(orientation.equals("Landscape")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        pdfLayout = findViewById(R.id.pdfLayout);
        layoutManager = new LinearLayoutManager(this);
        pdfLayout.setLayoutManager(layoutManager);
        try {
            openPdfRenderer();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF file", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        pdfAdapter = new PdfAdapter();
        pdfLayout.setAdapter(pdfAdapter);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        pdfLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    current_page = firstVisibleItemPosition + 1;
                    updateToast();
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
            if (position == total_pages) { // Insert blank page at the 3rd position (index 2)
                holder.bindBlankPage();
            } else {
                holder.bind(position);
            }
        }
        @Override
        public int getItemCount() {
            return total_pages +1;
        }
        class PdfViewHolder extends RecyclerView.ViewHolder {
            ImageView imageViewPdf;
            PdfViewHolder(View itemView) {
                super(itemView);
                imageViewPdf = itemView.findViewById(R.id.imageViewPdf);
            }
            void bind(int position) {
                if (pdfRenderer != null) {
                    PdfRenderer.Page page = pdfRenderer.openPage(position);
                    int bitmapWidth = page.getWidth() * 2;
                    int bitmapHeight = page.getHeight() * 2;
                    Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                    Rect rect = new Rect(0, 0, bitmapWidth, bitmapHeight);
                    page.render(bitmap, rect, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    imageViewPdf.setImageBitmap(bitmap);
                    page.close();
                }
            }
            void bindBlankPage() {
                Bitmap blankBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(blankBitmap);
                canvas.drawColor(Color.WHITE);
                imageViewPdf.setImageBitmap(blankBitmap);
            }
        }
    }
    private void scrollToPage(int page) {
        if (page > 0 && page <= total_pages) {
            pdfLayout.scrollToPosition(page - 1);
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
        SharedPreferences prefs = getSharedPreferences("ShredPreferenceJsonData", MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString("libraryJsonArray", "[]"));
            JSONObject json = jsonArray.getJSONObject(position);
            json.put("page", current_page);
            json.put("total_pages", total_pages);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("libraryJsonArray", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toast.setText("[ Progress Saved ]");
        toast.show();
    }
}
