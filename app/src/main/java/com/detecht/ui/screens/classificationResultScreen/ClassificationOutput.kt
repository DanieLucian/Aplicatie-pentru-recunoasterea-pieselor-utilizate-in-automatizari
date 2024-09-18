package com.detecht.ui.screens.classificationResultScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.detecht.data.daos.HistoryDao.DeviceWithScore

@Composable
fun ClassificationOutput(outputs: List<DeviceWithScore>?) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val maxOutput = outputs?.maxByOrNull { it.score } ?: return
    val textColor = Color.White
    val expandAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 100), label = "Fade"
    )
    val collapseAlpha by animateFloatAsState(
        targetValue = if (expanded) 0f else 1f,
        animationSpec = tween(durationMillis = 100), label = "Fade"
    )
    
    Column(
        modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300, easing = LinearOutSlowInEasing
                    )
                )
    ) {
        if (!expanded) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                        .clickable { expanded = !expanded }
                        .graphicsLayer {
                            this.alpha = collapseAlpha
                        }
            ) {
                Spacer(modifier = Modifier.size(34.dp))
                maxOutput.let {
                    Text(
                        color = textColor,
                        text = it.label,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                                .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Expand",
                        modifier = Modifier.size(34.dp),
                        tint = Color.Gray
                    )
                }
            }
        }
        if (expanded) {
            LazyColumn(modifier = Modifier
                    .graphicsLayer { this.alpha = expandAlpha }
                    .weight(1f)) {
                items(outputs) { output ->
                    Row(modifier = Modifier.padding(bottom = 8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.drawBehind {
                                val borderSize = 0.5.dp.toPx()
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = borderSize
                                )
                            }
                        ) {
                            Text(
                                color = textColor,
                                text = output.label,
                                fontSize = 16.sp,
                                softWrap = true,
                                modifier = Modifier
                                        .padding(2.dp)
                                        .weight(1f)
                            
                            )
                            Text(
                                color = textColor,
                                text = "%.2f".format(output.score * 100.0f) + "%",
                                fontSize = 16.sp,
                                modifier = Modifier
                                        .padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                        .clickable { expanded = !expanded }
                        .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Collapse",
                    modifier = Modifier.size(34.dp),
                    tint = Color.Gray
                )
            }
            
        }
    }
}

@Preview
@Composable
fun OutputTextPreview() {
    val outputs = listOf(
        DeviceWithScore(1,"cat", 0.92f),
        DeviceWithScore(1,"doggo", 0.1f),
        DeviceWithScore(1,"dadf", 0.02f),
        DeviceWithScore(1,"ffff", 0.00321f),
        DeviceWithScore(1,"surrender", 0.0000042f)
    )
    ClassificationOutput(outputs)
}
