package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.Checkin
import br.com.fiap.softekmentalapp.model.CheckinSummary
import br.com.fiap.softekmentalapp.network.CheckinApiService

class CheckinRepository(
    private val api: CheckinApiService,
    private val tokenProvider: () -> String?
){
    suspend fun addCheckin(checkin: Checkin): Checkin{
        val token = "Bearer ${tokenProvider()}"
        return api.createCheckin(token,checkin)
    }

    suspend fun getAllCheckins():List<Checkin>{
        val token = "Bearer ${tokenProvider()}"
        return api.getCheckins(token)
    }

    suspend fun getCheckinSummary(): CheckinSummary{
        val token = "Bearer ${tokenProvider()}"
        return api.getCheckinSummary(token)
    }
}