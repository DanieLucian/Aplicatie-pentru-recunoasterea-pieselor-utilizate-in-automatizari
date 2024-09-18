package com.detecht.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.detecht.ui.screens.imageCaptureScreen.rotate
import com.detecht.ui.screens.mainScreen.EMPTY_IMAGE_URI
import com.detecht.util.Permission
import com.detecht.util.getRealPathFromURI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ExperimentalPermissionsApi
@Composable
fun GallerySelectScreen(
        modifier: Modifier = Modifier,
        saveClassification: (Context, Bitmap, String?) -> Unit,
        navigateToResult: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val bitmap = BitmapFactory.decodeFile(getRealPathFromURI(context, uri)).rotate(90f)
                
                val photoFile = File(
                    context.filesDir,
                    "${ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))}.jpg"
                )
                FileOutputStream(photoFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                saveClassification(context, bitmap, photoFile.path)
                navigateToResult()
            }
        }
        // TODO: Fix bug date is null
    )
    
    @Composable
    fun LaunchGallery() {
        LaunchedEffect(Unit) {
            launcher.launch("image/*")
        }
    }
    
    Permission(
        permission = Manifest.permission.READ_MEDIA_IMAGES,
        rationale = "You want to read from photo gallery, so I'm going to have to ask for permission.",
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("No Photo Gallery!")
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            )
                        }
                    ) {
                        Text("Open Settings")
                    }
                    //If they don't want to grant permissions, this button will result in going back
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            //onImageUri(EMPTY_IMAGE_URI)
                        }
                    ) {
                        Text("Use Camera")
                    }
                }
            }
        },
    ) {
        LaunchGallery()
    }
}
