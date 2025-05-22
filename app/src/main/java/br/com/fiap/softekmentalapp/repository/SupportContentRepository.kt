package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.ContentType
import br.com.fiap.softekmentalapp.model.SupportContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SupportContentRepository {
    private val localContents = listOf(
        SupportContent(
            title = "Como lidar com a ansiedade",
            description = "Dicas práticas para momentos de ansiedade.",
            type = ContentType.ARTICLE,
            url = "https://www.tuasaude.com/ansiedade/"
        ),
        SupportContent(
            title = "Exercício de respiração guiada",
            description = "Vídeo rápido para acalmar em 5 minutos.",
            type = ContentType.VIDEO,
            url = "https://www.youtube.com/watch?v=3bKuoH8CkFc"
        ),
        SupportContent(
            title = "Dica rápida: Pare, respire, reflita",
            description = "Pare por 30 segundos, respire e observe seus pensamentos.",
            type = ContentType.TIP
        )
    )

    suspend fun getAllContents(): List<SupportContent> {
        return try {
            withContext(Dispatchers.IO) {
                val apiContents = ApiClient.supportApiService.getSupportContents()
                localContents + apiContents.map {
                    SupportContent(
                        title = it.title,
                        description = it.description,
                        type = ContentType.ARTICLE,
                        url = it.url
                    )
                }
            }
        } catch (e: Exception) {
            localContents
        }
    }
}