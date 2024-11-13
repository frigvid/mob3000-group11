package no.usn.mob3000.domain.usecase.content.news

import no.usn.mob3000.domain.repository.content.INewsRepository

/**
 * Use case for deleting news.
 *
 * @author 258030
 * @created 2024-10-30
 */
class DeleteNewsUseCase(
    private val newsRepository: INewsRepository
) {
    suspend fun deleteNews(newsId: String): Result<Unit> {
        return newsRepository.deleteNews(newsId)
    }
}
