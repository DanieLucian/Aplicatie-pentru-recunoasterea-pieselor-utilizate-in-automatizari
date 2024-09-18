package com.detecht.ui

import com.detecht.data.daos.ClassificationDao
import com.detecht.data.daos.DeviceDao
import com.detecht.data.daos.HistoryDao
import com.detecht.data.entities.DeviceClassification

class DetechtRepository(
        private val classifDao: ClassificationDao,
        private val deviceDao: DeviceDao,
        private val historyDao: HistoryDao
) {
    
    suspend fun getDevices(devices: List<String>) = deviceDao.getDevices(devices)
    suspend fun getDetailedDevice(deviceId: Int) = deviceDao.getDetailedDevice(deviceId)
    suspend fun getPredictedDevice(classificationId: Int) = deviceDao.getPredictedDevice(classificationId)
    
    fun getClassificationHistoryByDateDesc() = historyDao.getClassificationHistory()
    fun getClassificationHistoryByDateAsc() = historyDao.getClassificationHistoryByDateAsc()
    fun getClassificationHistoryByScoreAsc() = historyDao.getClassificationHistoryByScoreAsc()
    fun getClassificationHistoryByScoreDesc() = historyDao.getClassificationHistoryByScoreDesc()
    
    suspend fun insertClassification(imageUri: String): Int {
        return classifDao.insertClassification(imageUri).toInt()
    }
    
    suspend fun deleteClassification(id: Int) {
        classifDao.deleteClassification(id)
    }
    
    suspend fun clearClassificationHistory() = classifDao.deleteAllClassifications()
    
    suspend fun insertDeviceClassification(deviceClassification: List<DeviceClassification>) {
        historyDao.insertDeviceClassification(deviceClassification)
    }
    
}