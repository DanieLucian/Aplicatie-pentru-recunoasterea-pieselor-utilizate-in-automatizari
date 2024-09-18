package com.detecht.ui.screens.historyScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.detecht.data.daos.HistoryDao

@Composable
fun HistoryItem(
        classification: HistoryDao.ClassificationHistoryItem,
        onItemSelected: (String, List<HistoryDao.DeviceWithScore>) -> Unit,
        deleteClassification: (Int) -> Unit,
) {
    val bestScore = classification.outputs.maxBy { it.score }
    val textColor = Color.LightGray
    val padding = 8.dp
    val smallPadding = 4.dp
    
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                StartToEnd -> {
                    deleteClassification(classification.classificationId)
                }
                Settled -> return@rememberSwipeToDismissBoxState false
                else -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .40f }
    )
    
    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier
                .padding(top = padding, bottom = padding)
                .clip(RoundedCornerShape(8.dp)),
        enableDismissFromEndToStart = false,
        backgroundContent = { DismissBoxBackground(dismissState) }
    ) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = padding)
                    .background(Color.DarkGray)
                    ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                        .weight(6f)
                        .clickable { onItemSelected(classification.imagePath, classification.outputs) },
                verticalAlignment = Alignment.CenterVertically,
                
                ) {
                Box(
                    modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp),
                        painter = rememberAsyncImagePainter(classification.imagePath.toUri()),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Captured image",
                    )
                }
                Column(modifier = Modifier.padding(smallPadding)) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bestScore.label,
                            fontSize = 14.sp,
                            softWrap = true,
                            modifier = Modifier
                                    .padding(2.dp)
                                    .weight(1f),
                            color = textColor
                        )
                        Text(
                            color = textColor,
                            text = "%.2f".format(bestScore.score * 100.0f) + "%",
                            fontSize = 16.sp,
                            modifier = Modifier
                                    .padding(vertical = 2.dp)
                        )
                    }
                    Text(
                        color = textColor,
                        text = "${classification.date}, ${classification.time}",
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
            }
            
            
        }
    }
}

@Composable
fun DismissBoxBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        StartToEnd -> Color(0xFFFF1744)
        else -> Color.Transparent
    }
    
    Row(
        modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
    }
}