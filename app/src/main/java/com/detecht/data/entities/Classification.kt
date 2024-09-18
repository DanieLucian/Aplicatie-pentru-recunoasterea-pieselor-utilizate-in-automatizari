package com.detecht.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Classification(
        @PrimaryKey(autoGenerate = true)
        val classificationId: Int = 0,
        val dateCreated: String,
        val timeCreated: String,
        val imagePath: String
)