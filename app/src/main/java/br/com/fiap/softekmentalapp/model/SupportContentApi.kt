package br.com.fiap.softekmentalapp.model

data class SupportContentApi(
    val id: Int,
    val title: String,
    val description: String,
    val url: String? = null
)