package com.detecht.ui.screens.historyScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.detecht.data.daos.HistoryDao.ClassificationHistoryItem
import com.detecht.data.daos.HistoryDao.DeviceWithScore

@Composable
fun ScanHistoryScreen(
        history: List<ClassificationHistoryItem>,
        updateCurrentClassification: (String, List<DeviceWithScore>) -> Unit,
        deleteClassification: (Int) -> Unit,
        navigateToResult: () -> Unit,
        changeSortType: (SortType) -> Unit,
        clearHistory: () -> Unit,
        currentSortType: SortType
) {
    
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SortHistoryDropdown(
                changeSortType = changeSortType,
                currentSortType = currentSortType,
                clearHistory = clearHistory
            )
        }) { paddingValues ->
        if (history.isNotEmpty()) {
            LazyColumn(Modifier.padding(paddingValues)) {
                itemsIndexed(
                    items = history,
                    key = { _, classification ->
                        classification.classificationId
                    }) { index, classification: ClassificationHistoryItem ->
                    HistoryItem(
                        classification = classification,
                        onItemSelected = { imagePath, outputs ->
                            updateCurrentClassification(imagePath, outputs)
                            navigateToResult()
                        },
                        deleteClassification = deleteClassification
                    )
                    
                    if (index < history.lastIndex) {
                        HorizontalDivider(
                            color = Color.LightGray,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            thickness = 2.dp
                        )
                    }
                }
            }
        }
        else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "No history found!",
                    fontSize = 25.sp,
                    color = Color.LightGray
                )
            }
        }
    }
    
}

//@Preview(showBackground = true)
//@Composable
//fun HistoryItemPreview() {
//    LazyColumn {
//        items(5) {
//            HistoryItem(
//                imagePath = "Empty",
//                scores = DeviceWithScore("Label", 0.9832f),
//                date = "17-06-2024",
//                time = "12:00:00"
//            )
//        }
//    }
//}