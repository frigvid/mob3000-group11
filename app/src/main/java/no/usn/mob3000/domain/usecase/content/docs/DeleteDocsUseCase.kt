package no.usn.mob3000.domain.usecase.content.docs

import no.usn.mob3000.data.repository.content.DocsRepository
import no.usn.mob3000.domain.repository.content.IDocsRepository

/**
 * Use case for deleting news.
 *
 * @author 258030
 * @created 2024-10-30
 */
class DeleteDocsUseCase(
    private val docsRepository: IDocsRepository = DocsRepository()
) {
    suspend fun deleteDocs(docsId: String): Result<Unit> {
        return docsRepository.deleteDocs(docsId)
    }
}
