package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.data.repository.content.NewsRepository
import no.usn.mob3000.domain.repository.INewsRepository

class DeleteNewsUseCase(
    private val newsRepository: INewsRepository = NewsRepository()
) {
    suspend fun deleteNews(newsId: String): Result<Unit> {
        return newsRepository.deleteNews(newsId)
    }
}
