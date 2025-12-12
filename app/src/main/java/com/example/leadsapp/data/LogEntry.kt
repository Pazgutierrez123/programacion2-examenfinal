package com.example.leadsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val message: String,
    val leadId: Int? = null,
    val timestamp: Long = System.currentTimeMillis()
)
