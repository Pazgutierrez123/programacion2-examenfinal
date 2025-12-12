package com.example.leadsapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.leadsapp.data.Lead
import com.example.leadsapp.data.LeadRepository
import com.example.leadsapp.data.LogEntry
import kotlinx.coroutines.launch
import android.util.Log

class LeadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LeadRepository(application)

    // ⭐ RENOMBRADO — ESTA ES LA QUE USARÁ EL LISTADO
    val leads: LiveData<List<Lead>> = repository.allLeads

    val allLogs: LiveData<List<LogEntry>> = repository.allLogs

    private val _selectedLead = MutableLiveData<Lead?>()
    val selectedLead: LiveData<Lead?> = _selectedLead

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadLead(id: Int) {
        if (id <= 0) {
            _selectedLead.value = null
            return
        }
        viewModelScope.launch {
            _selectedLead.postValue(repository.getLeadById(id))
        }
    }

    fun saveLead(
        id: Int?,
        name: String,
        phone: String,
        email: String?,
        notes: String?,
        photoPath: String?
    ) {
        if (name.isBlank()) {
            _message.value = "El nombre es obligatorio"
            return
        }
        if (phone.isBlank()) {
            _message.value = "El teléfono es obligatorio"
            return
        }

        viewModelScope.launch {
            if (id == null || id <= 0) {
                val lead = Lead(
                    name = name.trim(),
                    phone = phone.trim(),
                    email = email?.trim(),
                    notes = notes?.trim(),
                    photoPath = photoPath
                )
                val newLeadId = repository.insertLead(lead)
                val newLead = repository.getLeadById(newLeadId.toInt())
                if (newLead != null) {
                    try {
                        syncLead(newLead)
                    } catch (e: Exception) {
                        Log.e("LeadViewModel", "Error syncing new lead: ${e.message}", e)
                        _message.postValue("Error al sincronizar el nuevo lead: ${e.message}")
                    }
                }
                _message.postValue("Lead creado correctamente")
            } else {
                val existing = repository.getLeadById(id)
                if (existing != null) {
                    val updated = existing.copy(
                        name = name.trim(),
                        phone = phone.trim(),
                        email = email?.trim(),
                        notes = notes?.trim(),
                        photoPath = photoPath ?: existing.photoPath,
                        updatedAt = System.currentTimeMillis()
                    )
                    repository.updateLead(updated)
                    _message.postValue("Lead actualizado correctamente")
                }
            }
        }
    }

    fun syncLead(lead: Lead) {
        viewModelScope.launch {
            val ok = repository.syncLead(lead)
            _message.postValue(
                if (ok) "Lead sincronizado con el servidor"
                else "Error al sincronizar lead"
            )
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
