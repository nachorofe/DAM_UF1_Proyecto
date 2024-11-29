package com.example.travellogv2.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viaje")
data class Viaje(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lugar: String,
    val descripcion: String,
    val foto: String
)

