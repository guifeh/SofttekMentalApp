package br.com.fiap.softekmentalapp.repository

import br.com.fiap.softekmentalapp.model.ContentType
import br.com.fiap.softekmentalapp.model.SupportContent

object SupportContentRepository {
    fun getAllContents(): List<SupportContent> {
        return listOf(
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
    }
}
