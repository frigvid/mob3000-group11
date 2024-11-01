package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.data.repository.content.DocsRepository
import no.usn.mob3000.domain.model.DocsData

class FetchDocUseCase(
    private val fetchRepository: DocsRepository = DocsRepository()
) {
    suspend fun fetchDocumentations(): Result<List<DocsData>> {
        return fetchRepository.fetchDocuments()
    }
}


