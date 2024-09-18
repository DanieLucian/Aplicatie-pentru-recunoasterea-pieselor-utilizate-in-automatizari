package com.detecht.ui.screens.mainScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.times
import androidx.wear.compose.material3.ripple
import com.detecht.R

@Composable
fun DeTechtBottomBar(
        onCameraButtonClicked: () -> Unit,
        onScanHistoryButtonClicked: () -> Unit,
        onGalleryButtonClicked: () -> Unit,
) {
    
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarButton(
                onCameraButtonClicked = onCameraButtonClicked,
                buttonName = "Cameră",
                imageVector = ImageVector.vectorResource(R.drawable.outline_camera_alt_24)
            )
            BottomBarButton(
                onCameraButtonClicked = onGalleryButtonClicked,
                buttonName = "Galerie",
                imageVector = ImageVector.vectorResource(R.drawable.image_select)
            )
            BottomBarButton(
                onCameraButtonClicked = onScanHistoryButtonClicked,
                buttonName = "Istoric",
                imageVector = ImageVector.vectorResource(R.drawable.history)
            )
        }
    }
}

@Composable
fun BottomBarButton(
        onCameraButtonClicked: () -> Unit,
        buttonName: String,
        imageVector: ImageVector
) {
    BoxWithConstraints {
        val height = maxHeight
        Column(
            Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onCameraButtonClicked,
                        indication = ripple(bounded = false, radius = 0.75 * height, color = {
                            Color.hsv(
                                0f, 1f,
                                0f, 0f
                            )
                        }),
                    )
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = buttonName,
            )
            Text(buttonName)
            
        }
    }
}