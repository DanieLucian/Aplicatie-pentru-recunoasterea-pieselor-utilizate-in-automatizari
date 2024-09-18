package com.detecht.util

import android.content.Context
import android.graphics.Bitmap
import com.detecht.ml.MobilenetEletronicDevicesModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

data class DeviceWithScore(val label: String, val score: Float)

fun classify(context: Context, imageBitmap: Bitmap): List<DeviceWithScore>? {
    
    try {
        
        val scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, 224, 224, true)
        
        val labels = context.assets
                .open("labels.txt")
                .bufferedReader()
                .readLines()
        
        val input = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        input.loadBuffer(byteBuffer)

// Runs model inference and gets result.
        val model = MobilenetEletronicDevicesModel.newInstance(context)
        val outputs = model.process(input).outputFeature0AsTensorBuffer.floatArray.toList()
        val labelScorePairs =
                labels.mapIndexed { index, label -> DeviceWithScore(label, "%.5f".format(outputs[index]).toFloat())}
        
        //zip(outputs).sortedByDescending { it.second }.take(5)
// Releases model resources if no longer used.
        model.close()
        
        return labelScorePairs.sortedByDescending { it.score }.take(5)
    } catch (e: Exception) {
        return null
    }
}