package no.usn.mob3000.domain.usecase.content.docs

import no.usn.mob3000.domain.model.content.DocsUpdateData
import no.usn.mob3000.domain.repository.content.IDocsRepository

/**
 * Use case for updating docs.
 *
 * @author 258030
 * @created 2024-10-30
 */
class UpdateDocsUseCase (
    private val docsRepository: IDocsRepository
) {
    suspend fun execute(
        docsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val updatedData = DocsUpdateData(
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
    return docsRepository.updateDocs(docsId, updatedData)
    }
}
