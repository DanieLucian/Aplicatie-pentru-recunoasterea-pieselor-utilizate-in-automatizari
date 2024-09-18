package com.detecht.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ClassificationDao {
    @Transaction
    @Query(
        """insert into Classification (imagePath, dateCreated, timeCreated)
            values (:imagePath, date('now', 'localtime'), time('now', 'localtime'))"""
    )
    suspend fun insertClassification(imagePath: String): Long
    
    @Transaction
    @Query("delete from Classification where classificationId = :id")
    suspend fun deleteClassification(id: Int)
    
    @Transaction
    @Query("delete from Classification")
    suspend fun deleteAllClassifications()
}