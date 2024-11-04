package no.usn.mob3000.domain.usecase.content.docs

import no.usn.mob3000.data.repository.content.DocsRepository
import no.usn.mob3000.domain.model.content.DocsData

/**
 * Use case for fetching news. Functions as a bridge between the UI and Data layers.
 *
 * @param fetchRepository The repository handling docs operations.
 * @return Result<List<DocsData>>
 * @author 258030
 * @created 2024-10-30
 */
class FetchDocUseCase(
    private val fetchRepository: DocsRepository = DocsRepository()
) {
    suspend fun fetchDocumentations(): Result<List<DocsData>> {
        return fetchRepository.fetchDocuments()
    }
}
