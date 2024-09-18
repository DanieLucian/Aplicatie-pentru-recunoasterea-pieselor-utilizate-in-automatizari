package com.detecht.data

import com.detecht.ui.screens.historyScreen.SortType

enum class SortCode {
    DATE_ASC,
    DATE_DESC,
    SCORE_ASC,
    SCORE_DESC
}

val sortingOptions = listOf(
    SortType(SortCode.DATE_ASC, "Cele mai vechi"),
    SortType(SortCode.DATE_DESC, "Cele mai recente"),
    SortType(SortCode.SCORE_ASC, "Scor crescător"),
    SortType(SortCode.SCORE_DESC, "Scor descrescător")
)