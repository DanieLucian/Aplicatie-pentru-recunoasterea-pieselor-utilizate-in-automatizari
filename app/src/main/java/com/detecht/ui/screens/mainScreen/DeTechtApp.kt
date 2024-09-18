package com.detecht.ui.screens.mainScreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.detecht.Graph
import com.detecht.ui.DetechtViewModel
import com.detecht.ui.screens.GallerySelectScreen
import com.detecht.ui.screens.classificationResultScreen.ClassificationResultScreen
import com.detecht.ui.screens.deviceInfoScreen.DeviceInfoScreen
import com.detecht.ui.screens.historyScreen.ScanHistoryScreen
import com.detecht.ui.screens.imageCaptureScreen.ImageCaptureScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

enum class Routes {
    ImageCapture,
    GallerySelect,
    ScanHistory,
    ClassificationResult,
    DeviceInfo
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalCoilApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun DeTechtApp(
        navController: NavHostController = rememberNavController(),
        viewModel: DetechtViewModel = Graph.viewModel
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        containerColor = Color.DarkGray,
        bottomBar = {
            DeTechtBottomBar(
                onCameraButtonClicked = {
                    navController.navigate(Routes.ImageCapture.name) {
                        popUpToTop(navController)
                    }
                },
                onScanHistoryButtonClicked = {
                    navController.navigate(Routes.ScanHistory.name) {
                        popUpToTop(navController)
                    }
                },
                onGalleryButtonClicked = {
                    navController.navigate(Routes.GallerySelect.name) {
                        popUpToTop(navController)
                    }
                }
            )
        }
    ) { innerPadding -> //inner padding ensures that the screens inside the NavHost do not overlap with the bottom bar
        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.ImageCapture.name,
                modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
            ) {
                composable(Routes.ScanHistory.name) {
                    ScanHistoryScreen(
                        history = state.history,
                        navigateToResult = {
                            navController.navigate(Routes.ClassificationResult.name) {
                                popUpToTop(navController)
                            }
                        },
                        updateCurrentClassification = viewModel::updateCurrentClassification,
                        deleteClassification = viewModel::deleteClassification,
                        changeSortType = viewModel::updateSortType,
                        clearHistory = viewModel::clearHistory,
                        currentSortType = state.sortType
                    )
                }
                composable(Routes.GallerySelect.name) {
                    GallerySelectScreen(
                        saveClassification = viewModel::saveClassification,
                        navigateToResult = {
                            navController.navigate(Routes.ClassificationResult.name) {
                                popUpToTop(navController)
                                
                            }
                        })
                }
                composable(Routes.ImageCapture.name) {
                    ImageCaptureScreen(
                        navigateToResult = {
                            navController.navigate(Routes.ClassificationResult.name) {
                                popUpToTop(navController)
                            }
                            
                        },
                        saveClassification = viewModel::saveClassification
                    )
                }
                composable(Routes.ClassificationResult.name) {
                    ClassificationResultScreen(
                        imagePath = state.imagePath,
                        outputs = state.outputs,
                        navigateToDeviceInfo = {
                            navController.navigate(Routes.DeviceInfo.name) {
                            }
                        })
                }
                composable(Routes.DeviceInfo.name) {
                    DeviceInfoScreen(
                        device = state.predictedDevice!!,
                    )
                }
            }
        }
    }
}

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}

@Preview
@Composable
fun DeTechtBottomBarPreview() {
    DeTechtBottomBar(
        onCameraButtonClicked = {},
        onScanHistoryButtonClicked = {},
        onGalleryButtonClicked = {}
    )
}