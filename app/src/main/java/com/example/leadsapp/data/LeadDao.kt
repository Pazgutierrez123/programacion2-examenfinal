package com.example.leadsapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LeadDao {
    @Query("SELECT * FROM leads ORDER BY createdAt DESC")
    fun getAllLeads(): LiveData<List<Lead>>

    @Query("SELECT * FROM leads WHERE id = :id LIMIT 1")
    suspend fun getLeadById(id: Int): Lead?

    @Insert
    suspend fun insert(lead: Lead): Long

    @Update
    suspend fun update(lead: Lead)

    @Delete
    suspend fun delete(lead: Lead)
}
