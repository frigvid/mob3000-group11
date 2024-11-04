package no.usn.mob3000.domain.usecase.content.news

import no.usn.mob3000.domain.repository.content.INewsRepository

/**
 * Use case for inserting news.
 *
 * @author 258030
 * @created 2024-10-30
 */
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
