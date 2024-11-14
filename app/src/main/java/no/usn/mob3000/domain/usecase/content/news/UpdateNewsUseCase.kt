package no.usn.mob3000.domain.usecase.content.news

import no.usn.mob3000.domain.repository.content.INewsRepository

/**
 * Use case for updating news.
 *
 * @author 258030
 * @created 2024-10-30
 */
class UpdateNewsUseCase(
    private val newsRepository: INewsRepository
) {
    suspend fun execute(
        newsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        return newsRepository.updateNews(newsId, title, summary, content, isPublished)
    }
}
