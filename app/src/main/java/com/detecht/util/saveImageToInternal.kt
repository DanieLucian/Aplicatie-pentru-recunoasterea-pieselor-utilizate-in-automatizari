package com.detecht.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveImageToInternal(context: Context, bitmap: Bitmap, filename: String): String? {
    val directory = context.filesDir
    
    val file = File(directory, filename)
    
    var fileOutputStream: FileOutputStream? = null
    return try {
        fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        try {
            fileOutputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
