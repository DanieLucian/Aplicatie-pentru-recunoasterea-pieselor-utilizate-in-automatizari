package com.detecht.ui.screens.historyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.detecht.data.SortCode
import com.detecht.data.sortingOptions

@Composable
fun SortHistoryDropdown(
        changeSortType: (SortType) -> Unit,
        clearHistory: () -> Unit,
        currentSortType: SortType
) {
    var expanded by remember { mutableStateOf(false) }

    val dropDownColor = Color.LightGray
    val backColor = MaterialTheme.colorScheme.primary
    
    Row(
        modifier = Modifier
                .fillMaxWidth()
                .background(backColor),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = clearHistory) {
            Text(text = "Șterge tot", color = dropDownColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .clickable { expanded = !expanded }
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Text(
                text = currentSortType.name, color = dropDownColor, fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "ArrowDropDown",
                tint = dropDownColor
            )
        }
        DropdownMenu(
            modifier = Modifier,
            containerColor = Color.DarkGray,
            shape = RoundedCornerShape(8.dp),
            expanded = expanded,
            offset = DpOffset(1000.dp, (-1000).dp),
            shadowElevation = 12.dp,
            tonalElevation = 20.dp,
            onDismissRequest = { expanded = false },
        ) {
            sortingOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 10f)),
                            text = option.name,
                            fontSize = 20.sp,
                            textAlign = TextAlign.End,
                            color = dropDownColor
                        )
                    },
                    onClick = {
                        changeSortType(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun SortingOptionsDropdownMenuPreview() {
}

data class SortType(
        val code: SortCode,
        val name: String
)