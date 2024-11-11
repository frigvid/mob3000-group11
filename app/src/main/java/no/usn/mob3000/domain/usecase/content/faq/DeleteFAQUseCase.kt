package no.usn.mob3000.domain.usecase.content.faq

import no.usn.mob3000.data.repository.content.FAQRepository
import no.usn.mob3000.domain.repository.content.IFAQRepository

/**
 * Use case for deleting FAQ. Functions as a bridge between the UI and Data layers.
 *
 * @param faqRepository The repository handling faq operations.
 * @return Result<Unit>
 * @author 258030
 * @created 2024-10-30
 */
class DeleteFAQUseCase(
    private val faqRepository: IFAQRepository = FAQRepository()
) {
    suspend fun deleteFAQ(faqId: String): Result<Unit> {
        return faqRepository.deleteFAQ(faqId)
    }
}
