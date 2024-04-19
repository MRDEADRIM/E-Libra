package com.mr_deadrim.ebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManagerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mFileList;
    private Map<String, BitmapWorkerTask> mBitmapTasks;
    private Map<String, Bitmap> mBitmapCache;

    public FileManagerAdapter(Context context, ArrayList<String> fileList) {
        super(context, R.layout.file_list_layout, fileList);
        this.mContext = context;
        this.mFileList = fileList;
        this.mBitmapTasks = new HashMap<>();
        this.mBitmapCache = new HashMap<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.item_image);
            viewHolder.textView = convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.bitmapWorkerTask != null) {
                viewHolder.bitmapWorkerTask.cancel(true);
            }
        }

        String fileName = mFileList.get(position);
        File file = new File(fileName);

        viewHolder.imageView.setImageResource(android.R.drawable.stat_notify_sync);

        if (fileName.toLowerCase().endsWith(".pdf")) {
            viewHolder.imageView.setImageResource(R.drawable.pdf);
        }

        if(file.isDirectory()){
            viewHolder.imageView.setImageResource(R.drawable.folder);
        }

        if (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            if (mBitmapCache.containsKey(fileName)) {
                viewHolder.imageView.setImageBitmap(mBitmapCache.get(fileName));
            } else {
                BitmapWorkerTask task = new BitmapWorkerTask(viewHolder.imageView);
                viewHolder.bitmapWorkerTask = task;
                task.execute(fileName);
            }
        }

        viewHolder.textView.setText(file.getName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        BitmapWorkerTask bitmapWorkerTask;
    }

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String filePath;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            filePath = params[0];
            return decodeSampledBitmapFromFile(filePath, 100, 100); // Adjust dimensions as needed
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && imageViewReference != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    mBitmapCache.put(filePath, bitmap);
                    imageView.setImageBitmap(bitmap);
                }
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