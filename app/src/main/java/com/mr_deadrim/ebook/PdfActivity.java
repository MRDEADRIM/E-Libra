package com.mr_deadrim.ebook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener {

    public String storage;
    public int position,current_page,total_pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Intent intent = getIntent();
        storage = intent.getStringExtra("storage");
        position = intent.getIntExtra("position",0);
        current_page=intent.getIntExtra("page",0);
        File file = new File(storage);
        PDFView pdfView = findViewById(R.id.pdfbook);
        pdfView.fromFile(file)
                .defaultPage(current_page)
                .onPageChange(this)
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        current_page=page;
        total_pages=pageCount;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePage();
    }

    private void savePage() {
        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString("key", "[]"));
            JSONObject json = jsonArray.getJSONObject(position);
            json.put("page", current_page);
            json.put("total_pages",total_pages);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("key", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "page saved.", Toast.LENGTH_SHORT).show();
    }
}
