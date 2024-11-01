package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.data.repository.content.FAQRepository
import no.usn.mob3000.domain.repository.IFAQRepository

class DeleteFAQUseCase(
    private val faqRepository: IFAQRepository = FAQRepository()
) {
    suspend fun deleteFAQ(faqId: String): Result<Unit> {
        return faqRepository.deleteFAQ(faqId)
    }


}
