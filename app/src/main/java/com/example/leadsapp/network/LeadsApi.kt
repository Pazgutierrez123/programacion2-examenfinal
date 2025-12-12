package com.example.leadsapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LeadsApi {
    @POST(".")
    suspend fun sendLead(@Body lead: LeadDto): Response<Void>
}
