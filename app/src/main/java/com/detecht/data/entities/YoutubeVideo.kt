package com.detecht.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Device::class,
            parentColumns = ["deviceId"],
            childColumns = ["deviceId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class YoutubeVideo(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val videoId: String,
        val title: String,
        @ColumnInfo(index = true) val deviceId: Int
)