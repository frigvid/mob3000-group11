package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.domain.model.FaqUpdateData
import no.usn.mob3000.domain.repository.IFAQRepository

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
