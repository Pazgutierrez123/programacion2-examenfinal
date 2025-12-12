package com.example.leadsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Lead::class, LogEntry::class], version = 1, exportSchema = false)
abstract class LeadsDatabase : RoomDatabase() {

    abstract fun leadDao(): LeadDao
    abstract fun logDao(): LogDao

    companion object {
        @Volatile
        private var INSTANCE: LeadsDatabase? = null

        fun getInstance(context: Context): LeadsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeadsDatabase::class.java,
                    "leads_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
