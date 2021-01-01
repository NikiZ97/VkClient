package com.sharonovnik.homework_2.data.local.storage

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.sharonovnik.homework_2.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

class ImagesManager @Inject constructor(private val context: Context) {

    private companion object {
        private const val CACHE_DIRECTORY_NAME = "images"
    }

    fun saveImageToGallery(drawable: Drawable, finally: () -> Unit = {}) {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val outputStream: OutputStream
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    Calendar.getInstance().time.toString() + ".jpg"
                )
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return
            outputStream = resolver.openOutputStream(imageUri)!!
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val image = File(imagesDir, Calendar.getInstance().time.toString() + ".jpg")
            outputStream = FileOutputStream(image)
        }
        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            finally()
        }
    }

    fun shareImage(drawable: Drawable, openChooser: (Intent) -> Unit) {
        saveImageToCachedStorage(drawable)
        val imagePath = File(context.cacheDir, CACHE_DIRECTORY_NAME)
        val newFile = File(imagePath, "image.png")
        val contentUri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider", newFile
        )
        contentUri?.let {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                setDataAndType(it, context.contentResolver.getType(it))
                putExtra(Intent.EXTRA_STREAM, contentUri)
            }
            openChooser(sendIntent)
        }
    }

    private fun saveImageToCachedStorage(drawable: Drawable) {
        try {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val cachePath = File(context.cacheDir, CACHE_DIRECTORY_NAME)
            cachePath.mkdirs()
            FileOutputStream(cachePath.path + "/image.png").use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        } catch (e: Exception) {
            Log.d("storage_test", "Error occurred: ${e.message}")
        }
    }
}