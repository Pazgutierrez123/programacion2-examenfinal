package com.example.leadsapp.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.leadsapp.network.LeadDto
import com.example.leadsapp.network.RetrofitClient

class LeadRepository(context: Context) {

    private val db = LeadsDatabase.getInstance(context)
    private val leadDao = db.leadDao()
    private val logDao = db.logDao()

    val allLeads: LiveData<List<Lead>> = leadDao.getAllLeads()
    val allLogs: LiveData<List<LogEntry>> = logDao.getAllLogs()

    // -------------------------------------------------------------
    //  LOGGING seguro (no deja que la app crashee si falla el log)
    // -------------------------------------------------------------
    private suspend fun log(type: String, message: String, leadId: Int? = null) {
        try {
            logDao.insert(
                LogEntry(
                    type = type,
                    message = message,
                    leadId = leadId
                )
            )
        } catch (e: Exception) {
            Log.e("LeadRepository", "Error insertando log: ${e.message}")
        }
    }

    // -------------------------------------------------------------
    //  INSERTAR Lead
    // -------------------------------------------------------------
    suspend fun insertLead(lead: Lead): Long {
        log("INFO", "Insertando lead: ${lead.name}")

        val id = leadDao.insert(lead)

        if (id > 0) {
            log("CREATED", "Lead creado correctamente: ${lead.name}", id.toInt())
        } else {
            log("ERROR", "Error al crear lead: ${lead.name}")
        }

        return id
    }

    // -------------------------------------------------------------
    //  ACTUALIZAR Lead
    // -------------------------------------------------------------
    suspend fun updateLead(lead: Lead) {
        leadDao.update(lead)
        log("UPDATED", "Lead actualizado: ${lead.name}", lead.id)
    }

    // -------------------------------------------------------------
    //  ELIMINAR Lead
    // -------------------------------------------------------------
    suspend fun deleteLead(lead: Lead) {
        leadDao.delete(lead)
        log("DELETED", "Lead eliminado: ${lead.name}", lead.id)
    }

    // -------------------------------------------------------------
    //  OBTENER Lead por ID
    // -------------------------------------------------------------
    suspend fun getLeadById(id: Int): Lead? {
        return leadDao.getLeadById(id)
    }

    // -------------------------------------------------------------
    //  SINCRONIZAR Lead con Webhook.site
    // -------------------------------------------------------------
    suspend fun syncLead(lead: Lead): Boolean {
        return try {

            // Convertimos Lead → LeadDto (formato para Retrofit)
            val dto = LeadDto(
                name = lead.name,
                phone = lead.phone,
                email = lead.email,
                notes = lead.notes,
                photoPath = lead.photoPath
            )

            // Llamada al servidor
            val response = RetrofitClient.api.sendLead(dto)

            if (response.isSuccessful) {

                // Marcamos el lead como sincronizado
                val updated = lead.copy(
                    synced = true,
                    updatedAt = System.currentTimeMillis()
                )

                leadDao.update(updated)

                log("SYNC", "Lead sincronizado: ${lead.name}", lead.id)

                true
            } else {

                // Error HTTP → queda registrado
                log(
                    "ERROR",
                    "Error al sincronizar lead ${lead.name}: código ${response.code()}",
                    lead.id
                )

                false
            }

        } catch (e: Exception) {

            log(
                "ERROR",
                "Excepción sincronizando lead ${lead.name}: ${e.message}",
                lead.id
            )

            false
        }
    }
}
