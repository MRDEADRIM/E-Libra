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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private static JSONArray settingJsonArray;
    String textStyle="sans-serif";
    int textSize=40;

    public FileManagerAdapter(Context context, ArrayList<String> fileList, JSONArray settingJsonArray) {
        super(context, R.layout.file_list_layout, fileList);
        this.mContext = context;
        this.mFileList = fileList;
        this.mBitmapCache = new HashMap<>();
        this.mExecutor = Executors.newFixedThreadPool(5);
        this.settingJsonArray = settingJsonArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewItemImage = convertView.findViewById(R.id.textViewItemImage);
            viewHolder.textViewItemName = convertView.findViewById(R.id.textViewItemName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String fileName = mFileList.get(position);
        File file = new File(fileName);
        String fileType = getFileType(file);
        viewHolder.textViewItemImage.setImageResource(android.R.drawable.stat_notify_sync);
        if ("pdf".equals(fileType)) {
            viewHolder.textViewItemImage.setImageResource(R.drawable.pdf);
        }
        if (file.isDirectory()) {
            viewHolder.textViewItemImage.setImageResource(R.drawable.folder);
        }
        if ("png".equals(fileType) || "jpg".equals(fileType)) {
            if (mBitmapCache.containsKey(fileName)) {
                viewHolder.textViewItemImage.setImageBitmap(mBitmapCache.get(fileName));
            } else {
                BitmapWorkerTask task = new BitmapWorkerTask(viewHolder.textViewItemImage, fileName);
                mExecutor.execute(task);
            }
        }

        if ("zip".equals(fileType)) {
            viewHolder.textViewItemImage.setImageResource(R.drawable.zip);
        }

        try{
            if(settingJsonArray.length()>1) {
                JSONObject jsonObject0 = settingJsonArray.getJSONObject(0);
                textStyle = jsonObject0.getString("style");
                textSize = jsonObject0.getInt("size");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        viewHolder.textViewItemName.setText(file.getName());
        Typeface typeface = Typeface.create(textStyle, Typeface.NORMAL);
        viewHolder.textViewItemName.setTypeface(typeface);
        viewHolder.textViewItemName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        return convertView;
    }
    public static String getFileType(File file) {
        if (file.isDirectory()) {
            return "directory";
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[16]; // Read the first 16 bytes of the file
            if (fis.read(header) != -1) {
                return getFileTypeFromHeader(header);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    private static String getFileTypeFromHeader(byte[] header) {
        // Check for PDF
        if (header.length >= 5 && header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46) {
            return "pdf";
        }
        // Check for PNG
        if (header.length >= 8 && header[0] == (byte) 0x89 && header[1] == (byte) 0x50 && header[2] == (byte) 0x4E && header[3] == (byte) 0x47) {
            return "png";
        }
        // Check for JPG
        if (header.length >= 2 && header[0] == (byte) 0xFF && header[1] == (byte) 0xD8) {
            return "jpg";
        }
        // Check for ZIP
        if (header.length >= 4 && header[0] == (byte) 0x50 && header[1] == (byte) 0x4B && header[2] == (byte) 0x03 && header[3] == (byte) 0x04) {
            return "zip";
        }
        // Add more file types here as needed
        return "unknown";
    }

    private static class ViewHolder {
        ImageView textViewItemImage;
        TextView textViewItemName;
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