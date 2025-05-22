package br.com.fiap.softekmentalapp.data

import br.com.fiap.softekmentalapp.model.SupportContentApi
import retrofit2.http.GET

interface SupportApiService {
    @GET("support_contents")
    suspend fun getSupportContents(): List<SupportContentApi>
}