package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.data.repository.content.DocsRepository
import no.usn.mob3000.domain.model.DocsData

/**
 * Use case for fetching news.
 *
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


