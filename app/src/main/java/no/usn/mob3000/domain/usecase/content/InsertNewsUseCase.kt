package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.domain.repository.INewsRepository

class InsertNewsUseCase(
    private val newsRepository: INewsRepository
) {
    suspend fun execute(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String? = null
    ): Result<Unit> {
        return newsRepository.insertNews(title, summary, content, isPublished, userId)
    }
}
