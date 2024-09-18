package com.detecht.ui.screens.classificationResultScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.detecht.data.daos.HistoryDao.DeviceWithScore

@Composable
fun ClassificationResultScreen(
        imagePath: String,
        outputs: List<DeviceWithScore>,
        navigateToDeviceInfo: () -> Unit
) {
    Box(
        modifier = Modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                    .padding(top = 14.dp, start = 14.dp, end = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imagePath != "") {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier,
                ) {
                    Box(
                        modifier = Modifier
                                .weight(3f)
                                .padding(bottom = 2.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        ClassificationOutput(outputs = outputs)
                    }
                    Box(
                        modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                            painter = rememberAsyncImagePainter(imagePath.toUri()),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Captured image",
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                    ) {
                        Button(onClick = navigateToDeviceInfo ) {
                            Text("Detalii", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}