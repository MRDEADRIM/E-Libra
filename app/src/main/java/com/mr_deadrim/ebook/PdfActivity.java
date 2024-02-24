package com.mr_deadrim.ebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener {

    private SharedPreferences pdfReader;

    public static void startActivity(Context context, String jsonString) {
        Intent intent = new Intent(context, PdfActivity.class);
        intent.putExtra("jsonObject", jsonString);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonObject");

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            String name = jsonObject.getString("name");
            String storage = jsonObject.getString("storage");

            pdfReader = getSharedPreferences("PDFReader", Context.MODE_PRIVATE);
            File file = new File(storage);
            PDFView pdfView = findViewById(R.id.pdfbook);
            pdfView.fromFile(file)
                    .defaultPage(pdfReader.getInt("pages", 0))
                    .onPageChange(this)
                    .load();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        SharedPreferences.Editor edit = pdfReader.edit();
        edit.putInt("pages", page);
        edit.apply();
    }
}
