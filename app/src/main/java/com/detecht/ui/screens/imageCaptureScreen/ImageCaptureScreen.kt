package com.detecht.ui.screens.imageCaptureScreen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun ImageCaptureScreen(
        modifier: Modifier = Modifier,
        saveClassification: (Context, Bitmap, String) -> Unit,
        navigateToResult: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        CameraCapture(
            modifier = modifier,
            onImageBitmap = { bitmap, context, imagePath ->
                saveClassification(context, bitmap, imagePath)
                navigateToResult()
            })
    }
}
