package no.usn.mob3000.domain.usecase.content.news

import no.usn.mob3000.data.repository.content.NewsRepository
import no.usn.mob3000.domain.model.content.NewsData

/**
 * Use case for fetching news.
 *
 * @author 258030
 * @created 2024-10-30
 */
class FetchNewsUseCase(
    private val repository: NewsRepository
) {
    suspend fun fetchLocalNews(): Result<List<NewsData>> {
        return repository.fetchAllNewsLocal()
    }

    suspend fun refreshRoomFromNetwork(): Result<Unit> {
        return repository.refreshNewsFromNetwork()
    }
}

