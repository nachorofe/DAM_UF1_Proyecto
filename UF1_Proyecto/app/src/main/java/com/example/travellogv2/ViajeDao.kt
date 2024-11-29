package com.example.travellogv2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ViajeDao {
    // Métodos de la tabla viajes
    @Insert
    suspend fun insertarViaje(viaje: Viaje)

    @Query("SELECT * FROM viaje")
    suspend fun obtenerTodosLosViajes(): List<Viaje>

    @Query("SELECT * FROM viaje WHERE id = :id")
    suspend fun obtenerViajePorId(id: Int): Viaje?

    // Métodos de la tabla "viajes_pendientes"
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarViajePendiente(viajePendiente: ViajePendiente)

    @Query("SELECT * FROM viajes_pendientes")
    suspend fun obtenerViajesPendientes(): List<ViajePendiente>

    @Delete
    suspend fun eliminarViajePendiente(viajePendiente: ViajePendiente)

    @Query("DELETE FROM viajes_pendientes WHERE id = :id")
    suspend fun eliminarViajePendientePorId(id: Int)


}
