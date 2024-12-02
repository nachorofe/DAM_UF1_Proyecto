package com.example.travellogv2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travellogv2.database.entity.Viaje
import com.example.travellogv2.database.dao.ViajeDao
import com.example.travellogv2.database.entity.ViajePendiente

/*
@Database marca la clase como una base de datos
 La versión es 2 ya que fue necesario tras añadir la tabla de viajes pendientes
 En ExportSchema definimos si queremos exportar el esquema a un json
 */
@Database(entities = [Viaje::class, ViajePendiente::class], version = 2, exportSchema = false)
abstract class ViajeDatabase : RoomDatabase() { // Especificamos la bdd como RoomDatabase para que pueda usar las funcionalidades de Room

    abstract fun viajeDao(): ViajeDao // Abstract ya que Room no puede implementar los métodos DAO por sí mismo

    companion object {
        @Volatile
        private var INSTANCE: ViajeDatabase? = null

        // Este método crea la base de datos
        fun getDatabase(context: Context): ViajeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ViajeDatabase::class.java,
                    "viaje_database"
                )
                    .fallbackToDestructiveMigration() // En caso de conflictos en migraciones, se crea una nueva bdd
                    .build() // Se crea la instancia
                INSTANCE = instance
                instance
            }
        }
    }
}

