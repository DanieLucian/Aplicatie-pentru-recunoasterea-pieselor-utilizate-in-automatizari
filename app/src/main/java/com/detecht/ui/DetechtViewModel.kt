package com.detecht.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.detecht.data.SortCode
import com.detecht.data.daos.DeviceDao.DetailedDevice
import com.detecht.data.daos.HistoryDao.ClassificationHistoryItem
import com.detecht.data.daos.HistoryDao.DeviceWithScore
import com.detecht.data.entities.DeviceClassification
import com.detecht.data.sortingOptions
import com.detecht.ui.screens.historyScreen.SortType
import com.detecht.util.classify
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetechtViewModel(
        private val repository: DetechtRepository
) : ViewModel() {
    private val _isPlayerFullscreen = mutableStateOf(false)
    private val _sortType = MutableStateFlow(sortingOptions.find { it.code == SortCode.DATE_DESC }!!)
    
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _history = _sortType.flatMapLatest { sortType ->
        when (sortType.code) {
            SortCode.DATE_DESC -> repository.getClassificationHistoryByDateDesc()
            SortCode.DATE_ASC -> repository.getClassificationHistoryByDateAsc()
            SortCode.SCORE_DESC -> repository.getClassificationHistoryByScoreDesc()
            SortCode.SCORE_ASC -> repository.getClassificationHistoryByScoreAsc()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _predictedDevice = MutableStateFlow<DetailedDevice?>(null)
    private val _state = MutableStateFlow(PublicState())
    
    val isPlayerFullscreen: State<Boolean> = _isPlayerFullscreen
    var youTubePlayer: YouTubePlayer? = null
        private set
    
    val state = combine(_state, _history, _sortType, _predictedDevice)
    { state, history, sortType, predictedDevice ->
        state.copy(
            history = history,
            sortType = sortType,
            predictedDevice = predictedDevice
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), PublicState())
    
    fun enterFullscreen() {
        _isPlayerFullscreen.value = true
    }
    
    fun exitFullscreen() {
        _isPlayerFullscreen.value = false
    }
    
    fun deleteClassification(id: Int) {
        viewModelScope.launch {
            repository.deleteClassification(id)
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearClassificationHistory()
        }
    }
    
    fun updateSortType(sortType: SortType) {
        viewModelScope.launch {
            _sortType.emit(sortType)
        }
    }
    
    fun updateCurrentClassification(
            imagePath: String,
            outputs: List<DeviceWithScore>?,
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    imagePath = imagePath,
                    outputs = outputs ?: emptyList(),
                )
            }
        }
        updatePredictedDevice(deviceId = outputs?.firstOrNull()?.deviceId ?: 0)
    }
    
    private fun updatePredictedDevice(deviceId: Int) {
        viewModelScope.launch {
            _predictedDevice.emit(repository.getDetailedDevice(deviceId))
        }
    }
    
    fun saveClassification(context: Context, imageBitmap: Bitmap, imagePath: String?) {
        viewModelScope.launch {
            val topFiveOutputs = classify(context, imageBitmap)?.associateBy { it.label }
            if (topFiveOutputs != null) {
                val classificationId = repository.insertClassification(imagePath ?: "empty")
                val devices = repository.getDevices(topFiveOutputs.keys.toList())
                
                val deviceScores = devices.map { device ->
                    DeviceClassification(
                        classificationIdFk = classificationId,
                        deviceIdFk = device.deviceId,
                        score = topFiveOutputs[device.label]!!.score
                    )
                }
                
                repository.insertDeviceClassification(deviceScores)
                
                updateCurrentClassification(
                    imagePath = imagePath ?: "empty",
                    outputs = devices.map { device ->
                        DeviceWithScore(
                            deviceId = device.deviceId,
                            label = device.label,
                            score = topFiveOutputs[device.label]!!.score
                        )
                    }.sortedByDescending { it.score }
                )
            }
        }
    }
}

data class PublicState(
        val sortType: SortType = sortingOptions.first { it.code == SortCode.DATE_DESC },
        val history: List<ClassificationHistoryItem> = emptyList(),
        val imagePath: String = "",
        val outputs: List<DeviceWithScore> = emptyList(),
        val predictedDevice: DetailedDevice? = null,
)