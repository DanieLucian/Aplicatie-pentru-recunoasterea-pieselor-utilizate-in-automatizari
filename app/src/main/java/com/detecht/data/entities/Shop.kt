package com.detecht.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
        @PrimaryKey(autoGenerate = true)
        val shopId: Int = 0,
        val name: String,
)