package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.data.repository.content.FAQRepository
import no.usn.mob3000.domain.model.FAQData

class FetchFAQUseCase(
    private val fetchRepository: FAQRepository = FAQRepository()
) {
    suspend fun fetchFAQ(): Result<List<FAQData>> {
        return fetchRepository.fetchFAQ()
    }
}
