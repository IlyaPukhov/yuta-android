package com.yuta.app.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.InputStream
import java.nio.file.Files

object ImageUtils {

    fun rotateImage(inputStream: InputStream, filename: String): InputStream {
        val tempFile = File.createTempFile("temp", null)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        val exif = ExifInterface(tempFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
        val rotatedBitmap = rotateBitmap(bitmap, orientation)

        val extension = filename.substring(filename.lastIndexOf(".") + 1)

        val rotatedTempFile = File.createTempFile("rotatedTemp", ".$extension")
        rotatedTempFile.outputStream().use { rotatedOutputStream ->
            val validExtension = Bitmap.CompressFormat.entries
                .map { it.name }
                .firstOrNull { it.equals(extension.uppercase(), ignoreCase = true) }
                ?: "JPEG"

            rotatedBitmap.compress(Bitmap.CompressFormat.valueOf(validExtension), 100, rotatedOutputStream)
        }
        return Files.newInputStream(rotatedTempFile.toPath())
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }

            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }

            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }

        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }
}
