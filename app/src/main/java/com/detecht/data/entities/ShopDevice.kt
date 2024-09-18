package com.detecht.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["shopId", "deviceId", "url"],
    foreignKeys = [
        ForeignKey(
            entity = Shop::class,
            parentColumns = ["shopId"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Device::class,
            parentColumns = ["deviceId"],
            childColumns = ["deviceId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class ShopDevice(
        @ColumnInfo(index = true) val shopId: Int,
        @ColumnInfo(index = true) val deviceId: Int,
        val url: String,
)
