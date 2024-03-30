package com.ilyap.yuta.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {

    public static String getFileName(Context context, Uri uri) {
        if (uri == null) return null;

        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, new String[]{
                    OpenableColumns.DISPLAY_NAME
            }, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }
        return result;
    }
}