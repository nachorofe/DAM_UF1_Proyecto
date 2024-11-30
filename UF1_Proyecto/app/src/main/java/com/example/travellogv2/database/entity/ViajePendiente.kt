package com.example.travellogv2.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viajes_pendientes")
data class ViajePendiente(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID autogenerado
    val viaje: String // Lugar pendiente de visitar
)

