package com.example.travellogv2.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellogv2.database.entity.Viaje
import com.example.travellogv2.database.entity.ViajePendiente

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

    // Consulta para buscar un viaje pendiente por nombre
    @Query("SELECT * FROM viajes_pendientes WHERE LOWER(viaje) = :nombre")
    suspend fun obtenerViajePendientePorNombre(nombre: String): ViajePendiente?

    @Query("SELECT * FROM viajes_pendientes")
    suspend fun obtenerViajesPendientes(): List<ViajePendiente>

    @Delete
    suspend fun eliminarViajePendiente(viajePendiente: ViajePendiente)

    @Query("DELETE FROM viajes_pendientes WHERE id = :id")
    suspend fun eliminarViajePendientePorId(id: Int)

    // Método para eliminar un viaje por id
    @Query("DELETE FROM viaje WHERE id = :id")
    suspend fun eliminarViaje(id: Int)

}
