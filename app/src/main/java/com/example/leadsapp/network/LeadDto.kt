package com.example.leadsapp.network

data class LeadDto(
    val name: String,
    val phone: String,
    val email: String?,
    val notes: String?,
    val photoPath: String?
)
