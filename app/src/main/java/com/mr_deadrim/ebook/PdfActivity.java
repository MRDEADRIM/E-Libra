package com.mr_deadrim.ebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class PdfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String path = intent.getStringExtra("path");


//        pdfReader = this.getSharedPreferences("PDFReader", Context.MODE_PRIVATE);
//        String path="/sdcard/abc.pdf";
//        File file=new File(path);
//
//
//        PDFView pdfView = (PDFView) findViewById(R.id.pdfbook);
//        pdfView.fromFile(file)
//                .defaultPage(pdfReader.getInt("pages",0))
//                .onPageChange(this)
//                .load();

        Toast.makeText(this, path+name, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onPageChanged(int page, int pageCount) {
//        SharedPreferences.Editor edit = pdfReader.edit();
//        edit.putInt("pages",page);
//        edit.commit();
//    }
}