package no.usn.mob3000.data.repository.content.remote

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.content.remote.NewsDto
import no.usn.mob3000.data.model.content.local.NewsItemLocal
import no.usn.mob3000.data.repository.content.local.NewsRepositoryLocal
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.content.NewsDataSource
import no.usn.mob3000.domain.model.content.NewsData
import no.usn.mob3000.domain.repository.content.INewsRepository
import java.util.UUID

/**
 * Repository class responsible for managing operations related to the docs table. It uses [NewsDataSource] for fetching and handling
 * database actions. Via [INewsRepository] it makes a possible communication route with the UI domain layer, without the domain layer getting accidental access
 * to parts of the code it never was suppose to have. It also helps the application run smoother, as there are less dependencies between layers.
 *
 * @param authDataSource The data source for authentication-related operations.
 * @param newsDataSource The data source for news-related operations.
 * @param newsRepositoryLocal The local data handling for news-related operations.
 * @author 258030
 * @created 2024-10-30
 */
class NewsRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val newsDataSource: NewsDataSource = NewsDataSource(),
    private val newsRepositoryLocal: NewsRepositoryLocal
) : INewsRepository {
    /**
     * Fetches a list of all news to later be used for generating news-cards in the UI. It maps the fetched data to a domain model, so it can be used in the UI.
     *
     * @return a result containing a list of news.
     * @throws Exception if an error occurs during the fetching process.
     */
    override suspend fun fetchAllNewsLocal(): Result<List<NewsData>> {
        return try {
            val localNews = newsRepositoryLocal.fetchAllNews()
            Result.success(localNews.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches a list of all news to later be used for generating news-cards in the UI.
     * It maps the fetched data to a domain model, so it can be used in the UI.
     *
     * @return A result containing a list of news.
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun refreshNewsFromNetwork(): Result<Unit> {
        return try {
            val networkNewsList = newsDataSource.fetchAllNews()
            val localNewsList = networkNewsList.map { it.toLocalModel() }
            newsRepositoryLocal.clearAllNews()
            newsRepositoryLocal.insertNewsList(localNewsList)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes a news by its ID. The ID is directly fetched by what specific card has been opened from one of the main screens
     * [NewsScreen] -> [NewsDetailsScreen]
     *
     * @return A result indicating the success or failure of the deletion operation.
     * @throws Exception If an error occurs during the deletion process.
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
     * Updates a chosen news by its ID. The ID is directly fetched by what specific card has been opened from one of the main screens
     * [NewsScreen] -> [NewsDetailsScreen]. As long as the ewsId exist, the update instance starts. Without an actual UUID for the news
     * the update operation would fail, but to prevent unnecessary API-calls we use a validator here. There is a throw in [NewsDataSource],
     * but the validator also make us aware if the method for fetching the ID fails.
     *
     * @return A result indicating the success or failure of the update operation.
     * */
    override suspend fun updateNews(
        newsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val originalNews = newsDataSource.fetchNewsById(newsId)
        if (originalNews != null) {
            val updatedNewsDto = NewsDto(
                newsId = newsId,
                createdAt = originalNews.createdAt,
                modifiedAt = Clock.System.now(),
                createdByUser = originalNews.createdByUser,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            return newsDataSource.updateNews(newsId, updatedNewsDto)
        } else {
            return Result.failure(Exception("Original news data not found"))
        }
    }

    /**
     * Fetches a news by its ID. Used for populating the update screen and identify what row we are working with.
     *
     * @return A result containing the fetched news.
     * @throws Exception If an error occurs during the fetching process.
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
     * Inserts a new news.The database has an auto generated UUID for new rows, but we call it here for good measure. Not passing the value seems
     * to interfere with the null exception. It displays the time of creation using [kotlinx.datetime.Clock] and tracks what user made the row.
     *
     * @return A result indicating the success or failure of the insertion operation.
     * @throws Exception If an error occurs during the insertion process.
     */
    override suspend fun insertNews(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
    ): Result<Unit> {
        val newsItem = NewsDto(
            newsId = UUID.randomUUID().toString(),
            createdAt = Clock.System.now(),
            modifiedAt = Clock.System.now(),
            createdByUser = authDataSource.getCurrentUserId(),
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return newsDataSource.insertNews(newsItem)
    }

    /**
     * Maps a NewsDto to a NewsData. For usage in the domain layer. Might be moved if repository are made abstract.
     */
    private fun NewsDto.toDomainModel(): NewsData {
    return NewsData(
        title = this.title ?: "",
        summary = this.summary ?: "",
        content = this.content ?: "",
        isPublished = this.isPublished,
        createdAt = this.createdAt,
        modifiedAt = this.modifiedAt,
        createdByUser = this.createdByUser ?: "",
        newsId = this.newsId
        )
    }

    private fun NewsDto.toLocalModel(): NewsItemLocal {
        return NewsItemLocal(
            newsId = this.newsId,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser ?: "",
            title = this.title ?: "",
            summary = this.summary ?: "",
            content = this.content ?: "",
            isPublished = this.isPublished
        )
    }

    private fun NewsItemLocal.toDomainModel(): NewsData {
        return NewsData(
            newsId = this.newsId,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser,
            title = this.title,
            summary = this.summary,
            content = this.content,
            isPublished = this.isPublished
        )
    }
}
