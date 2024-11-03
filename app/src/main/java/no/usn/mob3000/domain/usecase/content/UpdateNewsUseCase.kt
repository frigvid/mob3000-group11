package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.domain.model.NewsUpdateData
import no.usn.mob3000.domain.repository.INewsRepository

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
        val updatedData = NewsUpdateData(
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return newsRepository.updateNews(newsId, updatedData)
    }
}
