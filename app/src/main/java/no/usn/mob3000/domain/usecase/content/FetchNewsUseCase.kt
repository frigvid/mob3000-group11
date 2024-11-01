package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.data.repository.content.NewsRepository
import no.usn.mob3000.domain.model.NewsData

class FetchNewsUseCase(
    private val fetchRepository: NewsRepository = NewsRepository()
) {
    suspend fun fetchNews(): Result<List<NewsData>> {
        return fetchRepository.fetchNews()
    }
}
