package com.mr_deadrim.ebook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

public class FileUtils {
    public static String getPathFromURI(final Context context, final Uri uri) {
        String filePath = null;
        if (uri != null) {
            // Check if the URI is a file:// scheme
            if ("file".equals(uri.getScheme())) {
                filePath = uri.getPath();
            } else if ("content".equals(uri.getScheme())) {
                // For content URIs, differentiate between Documents and Downloads
                if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    filePath = getFilePathFromDownloadsUri(context, uri);
                } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                    filePath = getFilePathFromMediaUri(context, uri);
                }
            }
        }
        return filePath;
    }

    private static String getFilePathFromDownloadsUri(Context context, Uri uri) {

        String filePath = null;
        String[] projection = { MediaStore.Downloads.DATA };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA);
                filePath = cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

//    private static String getFilePathFromMediaUri(Context context, Uri uri) {
//        Toast.makeText(context, "Document", Toast.LENGTH_SHORT).show();
//        String filePath = null;
//        String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };
//        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
//            if (cursor != null && cursor.moveToFirst()) {
//                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
//                filePath = cursor.getString(index);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return filePath;
//    }
//    public static String moveToCacheFromUri(Context context, Uri uri) {
//        String path = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                if (columnIndex != -1) {
//                    String fileName = cursor.getString(columnIndex);
//                    if (fileName != null) {
//                        File file = new File(context.getCacheDir(), fileName);
//                        try {
//                            InputStream inputStream = context.getContentResolver().openInputStream(uri);
//                            if (inputStream != null) {
//                                FileOutputStream outputStream = new FileOutputStream(file);
//                                byte[] buffer = new byte[1024];
//                                int length;
//                                while ((length = inputStream.read(buffer)) > 0) {
//                                    outputStream.write(buffer, 0, length);
//                                }
//                                outputStream.close();
//                                inputStream.close();
//                                path = file.getAbsolutePath();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                cursor.close();
//            }
//        } else if (uri.getScheme().equals("file")) {
//            path = uri.getPath();
//        }
//        return path;
//    }

}
