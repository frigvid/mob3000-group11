package no.usn.mob3000.domain.usecase.content.docs

import no.usn.mob3000.domain.repository.content.IDocsRepository

/**
 * Use case for updating docs. Functions as a bridge between the UI and Data layers.
 *
 * @param docsRepository The repository handling docs operations.
 * @return Result<Unit>
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
    return docsRepository.updateDocs(docsId, title, summary, content, isPublished)
    }
}
