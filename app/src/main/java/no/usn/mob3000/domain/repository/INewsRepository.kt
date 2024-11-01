package no.usn.mob3000.domain.repository

import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.model.NewsUpdateData

interface INewsRepository {
    suspend fun fetchNews(): Result<List<NewsData>>
    suspend fun fetchNewsById(newsId: String): Result<NewsData?>
    suspend fun deleteNews(newsId: String): Result<Unit>
    suspend fun updateNews(newsId: String, updatedData: NewsUpdateData): Result<Unit>
}


