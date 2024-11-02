package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.domain.repository.IFAQRepository

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
