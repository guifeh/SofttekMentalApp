package br.com.fiap.softekmentalapp.model

data class SupportContent(
    val title: String,
    val description: String,
    val type: ContentType,
    val url: String? = null
)

enum class ContentType {
    ARTICLE, VIDEO, TIP
}
