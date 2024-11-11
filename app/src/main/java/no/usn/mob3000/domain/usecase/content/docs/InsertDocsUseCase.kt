package no.usn.mob3000.domain.usecase.content.docs

import no.usn.mob3000.domain.repository.content.IDocsRepository

/**
 * Use case for inserting docs. Functions as a bridge between the UI and Data layers.
 *
 * @param docsRepository The repository handling docs operations.
 * @return Result<Unit>
 * @author 258030
 * @created 2024-10-30
 */
class InsertDocsUseCase(
    private val docsRepository: IDocsRepository
) {
    suspend fun execute(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        return docsRepository.insertDocs(title, summary, content, isPublished)
    }
}
