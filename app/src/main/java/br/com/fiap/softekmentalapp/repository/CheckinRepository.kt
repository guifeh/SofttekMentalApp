package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.CheckinRequest
import br.com.fiap.softekmentalapp.model.CheckinResponse
import br.com.fiap.softekmentalapp.model.CheckinSummaryResponse
import br.com.fiap.softekmentalapp.network.RetrofitInstance
import br.com.fiap.softekmentalapp.network.UserApi

class CheckinRepository {
    private val api = RetrofitInstance.checkinApi

    suspend fun createCheckin(token: String, request: CheckinRequest): CheckinResponse{
        return api.createCheckin("Bearer $token", request)
    }

    suspend fun getCheckins(token: String): List<CheckinResponse>{
        return api.getCheckins("Bearer $token")
    }

    suspend fun deleteCheckin(token: String, id: String){
        return api.deleteCheckin("Bearer $token", id)
    }

    suspend fun getReport(
        token: String,
        startDate: String,
        endDate: String,
        userId: String? = null
    ): List<CheckinResponse>{
        return api.getReport("Bearer $token", startDate, endDate, userId)
    }

    suspend fun getSummary(
        token: String,
        startDate: String,
        endDate: String,
        userId: String? = null
    ): CheckinSummaryResponse{
        return api.getSummary("Bearer $token", startDate, endDate, userId)
    }

}