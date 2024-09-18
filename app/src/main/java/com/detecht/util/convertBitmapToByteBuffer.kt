package com.detecht.util

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
    val width = bitmap.width
    val height = bitmap.height
    val mean = 127.5f
    val std = 127.5f
    val byteBuffer = ByteBuffer.allocateDirect(width * height * 3 * Float.SIZE_BYTES)
    byteBuffer.order(ByteOrder.nativeOrder())
    
    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
    var pixel = 0
    for (i in 0 until width) {
        for (j in 0 until height) {
            val pixelVal = pixels[pixel++]
            
            byteBuffer.putFloat(((pixelVal shr 16 and 0xFF) - mean) / std)
            byteBuffer.putFloat(((pixelVal shr 8 and 0xFF) - mean) / std)
            byteBuffer.putFloat(((pixelVal and 0xFF) - mean) / std)
            
        }
    }
    bitmap.recycle()
    
    return byteBuffer
}