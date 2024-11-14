package no.usn.mob3000.domain.usecase.content.docs

import no.usn.mob3000.data.repository.content.remote.DocsRepository
import no.usn.mob3000.domain.repository.content.IDocsRepository

/**
 * Use case for deleting news. Functions as a bridge between the UI and Data layers.
 *
 * @param docsRepository The repository handling docs operations.
 * @return Result<Unit>
 * @author 258030
 * @created 2024-10-30
 */
class DeleteDocsUseCase(
    private val docsRepository: IDocsRepository
) {
    suspend fun deleteDocs(docsId: String): Result<Unit> {
        return docsRepository.deleteDocs(docsId)
    }
}
