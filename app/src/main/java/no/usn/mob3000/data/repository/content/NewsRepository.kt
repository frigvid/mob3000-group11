package no.usn.mob3000.data.repository.content

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.content.NewsDataSource
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.model.NewsUpdateData
import no.usn.mob3000.domain.repository.INewsRepository

/**
 * Repository class responsible for managing news-related operations.
 *
 * @param newsDataSource The data source for news-related operations.
 * @author 258030
 * @created 2024-10-30
 */
class NewsRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val newsDataSource: NewsDataSource = NewsDataSource()
) : INewsRepository {

    /**
     * Fetches a list of all news.
     */
    override suspend fun fetchNews(): Result<List<NewsData>> {
        return try {
            val newsDtoList = newsDataSource.fetchAllNews()
            Result.success(newsDtoList.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes a news by its ID.
     */
    override suspend fun deleteNews(newsId: String): Result<Unit> {
        return try {
            newsDataSource.deleteNewsById(newsId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates a chosen news by its ID.
     */
    override suspend fun updateNews(newsId: String, updatedData: NewsUpdateData): Result<Unit> {
        val originalNews = newsDataSource.fetchNewsById(newsId)
        if (originalNews != null) {
            val updatedNewsDto = NewsDto(
                newsId = newsId,
                createdAt = originalNews.createdAt,
                modifiedAt = Clock.System.now(),
                createdByUser = originalNews.createdByUser,
                title = updatedData.title,
                summary = updatedData.summary,
                content = updatedData.content,
                isPublished = updatedData.isPublished
            )
            return newsDataSource.updateNews(newsId, updatedNewsDto, NewsDto.serializer())
        } else {
            return Result.failure(Exception("Original news data not found"))
        }
    }

    /**
     * Fetches a news by its ID.
     */
    override suspend fun fetchNewsById(newsId: String): Result<NewsData?> {
        return try {
            val newsDto = newsDataSource.fetchNewsById(newsId)
            Result.success(newsDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inserts a new news.
     */
    override suspend fun insertNews(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String?
    ): Result<Unit> {
        val newsItem = NewsDto(
            newsId = null,
            createdAt = Clock.System.now(),
            modifiedAt = Clock.System.now(),
            createdByUser = authDataSource.getCurrentUserId(),
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return newsDataSource.insertNews(newsItem, NewsDto.serializer())
    }


    /**
     * Maps a NewsDto to a NewsData. For usage in the domain layer.
     */
    private fun NewsDto.toDomainModel(): NewsData {
    return NewsData(
        title = this.title ?: "",
        summary = this.summary ?: "",
        content = this.content ?: "",
        isPublished = this.isPublished ?: false,
        createdAt = this.createdAt,
        modifiedAt = this.modifiedAt,
        createdByUser = this.createdByUser ?: "",
        newsId = this.newsId ?: ""
        )
    }
}




