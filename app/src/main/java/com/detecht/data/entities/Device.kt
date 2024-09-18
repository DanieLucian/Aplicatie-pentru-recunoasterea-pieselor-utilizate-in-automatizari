package com.detecht.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(
        @PrimaryKey(autoGenerate = true)
        val deviceId: Int = 0,
        val label: String,
        val description: String,
)
