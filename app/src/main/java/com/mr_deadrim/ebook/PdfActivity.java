package com.mr_deadrim.ebook;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Matrix;
import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PdfActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PdfRenderer pdfRenderer;
    private int pageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            openPdfRenderer();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF file", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PdfAdapter adapter = new PdfAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void openPdfRenderer() throws IOException {
        File file = new File("/sdcard/abc.pdf");
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        pageCount = pdfRenderer.getPageCount();
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
            return pageCount;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
    }
}