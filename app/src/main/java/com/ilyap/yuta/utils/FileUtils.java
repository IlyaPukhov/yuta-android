package com.ilyap.yuta.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.exifinterface.media.ExifInterface;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

@UtilityClass
public class FileUtils {
    private static final int MAX_BUFFER_SIZE = 1024 * 1024;


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

    @SneakyThrows
    public static InputStream rotateImage(InputStream inputStream, String fileName) {
        File tempFile = File.createTempFile("temp", null);
        @Cleanup FileOutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();

        ExifInterface exif = new ExifInterface(tempFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
        Bitmap rotatedBitmap = rotateBitmap(bitmap, orientation);

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        File rotatedTempFile = File.createTempFile("rotatedTemp", "." + extension);
        @Cleanup FileOutputStream rotatedOutputStream = new FileOutputStream(rotatedTempFile);

        String validExtension = Arrays.stream(Bitmap.CompressFormat.values())
                .map(Bitmap.CompressFormat::name)
                .filter(ext -> ext.equals(extension.toUpperCase()))
                .findFirst()
                .orElse("JPEG");

        rotatedBitmap.compress(Bitmap.CompressFormat.valueOf(validExtension), 100, rotatedOutputStream);
        return Files.newInputStream(rotatedTempFile.toPath());
    }

    @SneakyThrows
    private static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedBitmap;
    }
}