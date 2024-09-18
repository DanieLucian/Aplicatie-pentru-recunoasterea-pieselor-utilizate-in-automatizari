package com.detecht.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["classificationIdFk", "deviceIdFk"],
    foreignKeys = [
        ForeignKey(
            entity = Classification::class,
            parentColumns = ["classificationId"],
            childColumns = ["classificationIdFk"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Device::class,
            parentColumns = ["deviceId"],
            childColumns = ["deviceIdFk"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class DeviceClassification(
        @ColumnInfo(index = true) val classificationIdFk: Int,
        @ColumnInfo(index = true) val deviceIdFk: Int,
        val score: Float
)
