package no.usn.mob3000.domain.usecase.content.faq

import no.usn.mob3000.domain.repository.content.IFAQRepository

/**
 * Use case for inserting FAQ.
 *
 * @author 258030
 * @created 2024-10-30
 */
class InsertFAQUseCase(
    private val faqRepository: IFAQRepository
) {
    suspend fun execute(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String? = null
    ): Result<Unit> {
        return faqRepository.insertFAQ(title, summary, content, isPublished, userId)
    }
}
