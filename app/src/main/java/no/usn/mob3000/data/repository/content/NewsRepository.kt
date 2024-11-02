package no.usn.mob3000.data.repository.content

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.data.source.remote.content.NewsDataSource
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.model.NewsUpdateData
import no.usn.mob3000.domain.repository.INewsRepository

class NewsRepository(
    private val newsDataSource: NewsDataSource = NewsDataSource()
) : INewsRepository {

    override suspend fun fetchNews(): Result<List<NewsData>> {
        return try {
            val newsDtoList = newsDataSource.fetchAllNews()
            Result.success(newsDtoList.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNews(newsId: String): Result<Unit> {
        return try {
            newsDataSource.deleteNewsById(newsId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    override suspend fun fetchNewsById(newsId: String): Result<NewsData?> {
        return try {
            val newsDto = newsDataSource.fetchNewsById(newsId)
            Result.success(newsDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertNews(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String?
    ): Result<Unit> {
        val currentUserId = userId ?: "00ba54a6-c585-4871-905e-7d53262f05c1"
        val newsItem = NewsDto(
            newsId = null,
            createdAt = Clock.System.now(),
            modifiedAt = Clock.System.now(),
            createdByUser = currentUserId,
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return newsDataSource.insertNews(newsItem, NewsDto.serializer())
    }




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




