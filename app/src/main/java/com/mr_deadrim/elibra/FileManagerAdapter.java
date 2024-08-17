package com.mr_deadrim.elibra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileManagerAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final ArrayList<String> mFileList;
    private final Map<String, Bitmap> mBitmapCache;
    private final ExecutorService mExecutor;
    private static JSONArray json2Array;

    String style="sans-serif";
    int size=40;

    public FileManagerAdapter(Context context, ArrayList<String> fileList, JSONArray json2Array) {
        super(context, R.layout.file_list_layout, fileList);
        this.mContext = context;
        this.mFileList = fileList;
        this.mBitmapCache = new HashMap<>();
        this.mExecutor = Executors.newFixedThreadPool(5);

        this.json2Array = json2Array;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.textViewItemImage);
            viewHolder.textView = convertView.findViewById(R.id.textViewItemName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String fileName = mFileList.get(position);
        File file = new File(fileName);

        viewHolder.imageView.setImageResource(android.R.drawable.stat_notify_sync);

        if (fileName.toLowerCase().endsWith(".pdf")) {
            viewHolder.imageView.setImageResource(R.drawable.pdf);
        }
        if (file.isDirectory()) {
            viewHolder.imageView.setImageResource(R.drawable.folder);
        }
        if (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            if (mBitmapCache.containsKey(fileName)) {
                viewHolder.imageView.setImageBitmap(mBitmapCache.get(fileName));
            } else {
                BitmapWorkerTask task = new BitmapWorkerTask(viewHolder.imageView, fileName);
                mExecutor.execute(task);
            }
        }
        if (fileName.toLowerCase().endsWith(".zip")) {
            viewHolder.imageView.setImageResource(R.drawable.zip);
        }


        try{
            JSONObject jsonObject0 = json2Array.getJSONObject(0);
            style = jsonObject0.getString("style");
            size = jsonObject0.getInt("size");

        }catch(Exception e){
            e.printStackTrace();
        }


        viewHolder.textView.setText(file.getName());
        Typeface typeface = Typeface.create(style, Typeface.NORMAL);
        viewHolder.textView.setTypeface(typeface);

        viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);



        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;

    }

    private class BitmapWorkerTask implements Runnable {
        private final WeakReference<ImageView> imageViewReference;
        private final String filePath;

        public BitmapWorkerTask(ImageView imageView, String filePath) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.filePath = filePath;
        }

        @Override
        public void run() {
            final Bitmap bitmap = decodeSampledBitmapFromFile(filePath, 100, 100); // Adjust dimensions as needed
            if (bitmap != null && imageViewReference.get() != null) {
                mBitmapCache.put(filePath, bitmap);
                ((AppCompatActivity) mContext).runOnUiThread(() -> {
                    ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }
    }

    private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}