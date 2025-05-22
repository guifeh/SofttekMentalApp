package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.SupportContentApi
import retrofit2.http.GET

interface SupportApiService {
    @GET("posts")
    suspend fun getSupportContents(): List<SupportContentApi>
}