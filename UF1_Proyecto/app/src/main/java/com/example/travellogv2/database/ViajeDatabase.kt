package com.example.travellogv2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travellogv2.database.entity.Viaje
import com.example.travellogv2.database.dao.ViajeDao
import com.example.travellogv2.database.entity.ViajePendiente

@Database(entities = [Viaje::class, ViajePendiente::class], version = 2, exportSchema = false)
abstract class ViajeDatabase : RoomDatabase() {

    abstract fun viajeDao(): ViajeDao

    companion object {
        @Volatile
        private var INSTANCE: ViajeDatabase? = null

        fun getDatabase(context: Context): ViajeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ViajeDatabase::class.java,
                    "viaje_database"
                )
                    .fallbackToDestructiveMigration() // Manejo de cambios en la base de datos
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

