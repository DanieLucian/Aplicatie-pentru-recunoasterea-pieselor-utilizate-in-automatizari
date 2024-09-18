package com.detecht.data.daos

import androidx.room.Dao
import androidx.room.DatabaseView
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import com.detecht.data.entities.DeviceClassification
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Upsert
    suspend fun insertDeviceClassification(crossRef: List<DeviceClassification>)
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        select
        classificationId,
        imagePath,
        strftime('%d-%m-%Y', dateCreated) as date,
        timeCreated as time,
        max(score)
        from Classification
        join DeviceClassification on classificationId = classificationIdFk
        group by classificationId
        order by score asc
        """
    )
    fun getClassificationHistoryByScoreAsc(): Flow<List<ClassificationHistoryItem>>
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        select
        classificationId,
        imagePath,
        strftime('%d-%m-%Y', dateCreated) as date,
        timeCreated as time,
        max(score)
        from Classification
        join DeviceClassification on classificationId = classificationIdFk
        group by classificationId
        order by score desc
        """
    )
    fun getClassificationHistoryByScoreDesc(): Flow<List<ClassificationHistoryItem>>
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        select
        classificationId,
        imagePath,
        strftime('%d-%m-%Y', dateCreated) as date,
        timeCreated as time
        from Classification
        order by dateCreated desc, timeCreated desc
        """
    )
    fun getClassificationHistory(): Flow<List<ClassificationHistoryItem>>
    
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        select
        classificationId,
        imagePath,
        strftime('%d-%m-%Y', dateCreated) as date,
        timeCreated as time
        from Classification
        order by dateCreated asc, timeCreated asc
        """
    )
    fun getClassificationHistoryByDateAsc(): Flow<List<ClassificationHistoryItem>>
    
    @RewriteQueriesToDropUnusedColumns
    @DatabaseView(
        """
        select * from DeviceClassification as dc
        join Device as d on d.deviceId = dc.deviceIdFk
        join Classification as c on c.classificationId = dc.classificationIdFk
        order by date(dateCreated) desc, time(timeCreated, 'localtime') desc, score desc
        """,
        viewName = "DeviceWithScoreView"
    )
    data class DeviceWithScoreView(
            val classificationId: Int,
            val deviceId: Int,
            val label: String,
            val score: Float,
    )
    
    data class DeviceWithScore(
            val deviceId: Int,
            val label: String,
            val score: Float,
    )
    
    data class ClassificationHistoryItem(
            val classificationId: Int,
            val imagePath: String,
            val date: String?,
            val time: String,
            @Relation(
                parentColumn = "classificationId",
                entityColumn = "classificationId",
                entity = DeviceWithScoreView::class,
            )
            val outputs: List<DeviceWithScore>
    )
}

