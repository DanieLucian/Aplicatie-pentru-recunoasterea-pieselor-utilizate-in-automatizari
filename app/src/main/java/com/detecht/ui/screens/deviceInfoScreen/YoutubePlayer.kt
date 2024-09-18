package com.detecht.ui.screens.deviceInfoScreen

import android.view.View
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubeMiniPlayer(
        youtubeVideoId: String,
        lifecycleOwner: LifecycleOwner,
) {
    val youTubePlayerCallback = object : YouTubePlayerCallback {
        override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
            youTubePlayer.cueVideo(youtubeVideoId, 0f)
        }
    }
    val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
    }

    val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .ccLoadPolicy(1)
            .fullscreen(1)
            .build()
    
    AndroidView(
        modifier = Modifier
                .clip(RoundedCornerShape(4.dp)),
        factory = { context ->
            val youTubePlayerView = YouTubePlayerView(context = context, defStyleAttr = 1)
            youTubePlayerView.apply {
                lifecycleOwner.lifecycle.addObserver(this)
                
                enableAutomaticInitialization = false
                
                initialize(youTubePlayerListener, iFramePlayerOptions)
                isVerticalScrollBarEnabled = true
            }
        },
        update = {
            it.getYouTubePlayerWhenReady(youTubePlayerCallback)
        })
}


//@Preview
//@Composable
//fun YouTubePlayerPreview() {
//    YouTubePlayer(
//        youtubeVideoId = "FgAL6T_KILw",
//        lifecycleOwner = LocalLifecycleOwner.current,
//        null
//    )
//}