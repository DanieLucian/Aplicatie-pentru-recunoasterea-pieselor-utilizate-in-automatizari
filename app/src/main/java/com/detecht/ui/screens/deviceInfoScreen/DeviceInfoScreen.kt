package com.detecht.ui.screens.deviceInfoScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.detecht.data.daos.DeviceDao.DetailedDevice

@Composable
fun DeviceInfoScreen(
        device: DetailedDevice,
) {
    var currentVideoId by remember { mutableStateOf(device.videos.firstOrNull()?.videoId ?: "") }
    val context = LocalContext.current
    val textColor = Color.LightGray
    val backColor = Color.DarkGray
    
    val textStyle = TextStyle(
        color = textColor,
        fontWeight = FontWeight.Bold,
        lineHeight = TextUnit(16f, TextUnitType.Sp),
        textAlign = TextAlign.Start,
    )
    
    Scaffold(
        containerColor = backColor,
        topBar = {
            Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(device.label, style = textStyle, modifier = Modifier.padding(10.dp), fontSize = 20.sp)
            }
        },
    ) { innerPadding ->
        val paddingOffset = 10.dp
        val padding = PaddingValues(
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + paddingOffset,
            top = innerPadding.calculateTopPadding() + paddingOffset,
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + paddingOffset,
            bottom = innerPadding.calculateBottomPadding() + paddingOffset
        )
        Column(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = "Informații generale:", style = textStyle, fontSize = 18.sp)
            
            Text(
                device.description, style = textStyle, fontWeight = FontWeight.Normal
            )
            
            Column {
                Text("Resurse educaționale:", style = textStyle, fontSize = 18.sp)
                device.videos.forEach { video ->
                    TextButton(onClick = { currentVideoId = video.videoId }) {
                        Text(video.title, color = Color.Cyan, modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp))
                        Spacer(Modifier)
                    }
                }
            }
            
            YouTubeMiniPlayer(
                youtubeVideoId = currentVideoId,
                lifecycleOwner = LocalLifecycleOwner.current,
            )
            
            
            Column {
                Text("Îl poți cumpăra de pe:", style = textStyle, fontSize = 18.sp)
                device.shops.forEach { shop ->
                    TextButton(
                        modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shop.deviceUrl))
                            context.startActivity(intent)
                        }) {
                        Text(
                            shop.shopName, color = Color.Cyan, style = textStyle,
                            modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                        )
                        Spacer(Modifier)
                    }
                }
            }
        }
    }
    
}
