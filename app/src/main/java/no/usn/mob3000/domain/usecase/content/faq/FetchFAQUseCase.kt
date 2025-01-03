package no.usn.mob3000.domain.usecase.content.faq

import no.usn.mob3000.data.repository.content.remote.FAQRepository
import no.usn.mob3000.domain.model.content.FAQData

/**
 * Use case for fetching FAQ. Functions as a bridge between the UI and Data layers.
 *
 * @param fetchRepository The repository handling faq operations.
 * @return Result<List<FAQData>>
 * @author 258030
 * @created 2024-10-30
 */
class FetchFAQUseCase(
    private val fetchRepository: FAQRepository
) {
    suspend fun fetchLocalFaq(): Result<List<FAQData>> {
        return fetchRepository.fetchAllFaqLocal()
    }

    suspend fun refreshRoomFromNetwork(): Result<Unit> {
        return fetchRepository.refreshFaqFromNetwork()
    }
}
