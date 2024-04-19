package com.mr_deadrim.ebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;

public class FileManagerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mFileList;

    public FileManagerAdapter(Context context, ArrayList<String> fileList) {
        super(context, R.layout.file_list_layout, fileList);
        this.mContext = context;
        this.mFileList = fileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.file_list_layout, parent, false);
        }

        ImageView imageView = listItemView.findViewById(R.id.item_image);
        TextView textView = listItemView.findViewById(R.id.item_name);
        String fileName = mFileList.get(position);

        File file = new File(fileName);

        if (fileName.toLowerCase().endsWith(".pdf")) {
            imageView.setImageResource(R.drawable.pdf);
        }

        if(file.isDirectory()){
            imageView.setImageResource(R.drawable.folder);
        }

        if (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            imageView.setImageURI(Uri.parse(fileName));
        }

        textView.setText(file.getName());
        return listItemView;
    }
}