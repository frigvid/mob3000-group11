package no.usn.mob3000.domain.usecase.content.faq

import no.usn.mob3000.domain.model.content.FaqUpdateData
import no.usn.mob3000.domain.repository.IFAQRepository

/**
 * Use case for updating FAQ.
 *
 * @author 258030
 * @created 2024-10-30
 */
class UpdateFAQUseCase(
    private val faqRepository: IFAQRepository
) {
    suspend fun execute(
        faqId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val updatedData = FaqUpdateData(
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return faqRepository.updateFAQ(faqId, updatedData)
    }
}
