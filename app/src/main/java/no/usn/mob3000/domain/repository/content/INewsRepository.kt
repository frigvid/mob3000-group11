package no.usn.mob3000.domain.repository.content

import no.usn.mob3000.domain.model.content.NewsData

/**
 * Interface for news repository.
 *
 * @author 258030
 * @created 2024-10-30
 */
interface INewsRepository {
    suspend fun fetchNewsById(newsId: String): Result<NewsData?>
    suspend fun deleteNews(newsId: String): Result<Unit>
    suspend fun updateNews(
        newsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit>
    suspend fun insertNews(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit>
    suspend fun refreshNewsFromNetwork(): Result<Unit>
    suspend fun fetchAllNewsLocal(): Result<List<NewsData>>

}


