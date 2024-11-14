package no.usn.mob3000.domain.viewmodel.content

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.content.NewsData
import no.usn.mob3000.domain.usecase.content.news.DeleteNewsUseCase
import no.usn.mob3000.domain.usecase.content.news.FetchNewsUseCase
import no.usn.mob3000.domain.usecase.content.news.InsertNewsUseCase
import no.usn.mob3000.domain.usecase.content.news.UpdateNewsUseCase

/**
 * ViewModel for news. Using the different usecases to communicate with the business logic handled in
 * the data layer.
 *
 * @author 258030
 * @contributor frigvid
 * @created 2024-11-04
 */
class NewsViewModel(
    private val fetchNewsUseCase: FetchNewsUseCase,
    private val updateNewsUseCase: UpdateNewsUseCase,
    private val insertNewsUseCase: InsertNewsUseCase,
    private val deleteNewsUseCase: DeleteNewsUseCase
) : ViewModel() {
    private var hasRefreshed = false

    private val _news = MutableStateFlow<Result<List<NewsData>>>(Result.success(emptyList()))
    val news: StateFlow<Result<List<NewsData>>> = _news

    private val _selectedNews = mutableStateOf<NewsData?>(null)
    val selectedNews: State<NewsData?> = _selectedNews

    init {
        periodicRefresh()
    }

    /**
     * Fetch news from the network and update the local database. It only calls from remote once per session. That means it does not call the remote db everytime
     * someone enters the news screen. Using a simple boolean to determine that. Resets every session (or with a refresh when we get to that)
     */
    fun fetchNews() {
        viewModelScope.launch {
            if (!hasRefreshed) {
                refreshRoomNews()
                hasRefreshed = true
            }
            loadLocalNews()
        }
    }

    suspend fun refreshRoomNews() {
        fetchNewsUseCase.refreshRoomFromNetwork()
    }

    suspend fun loadLocalNews() {
        _news.value = fetchNewsUseCase.fetchLocalNews()
    }

    /**
     * Inserts a new row into the news table with the provided parameters.
     *
     * @param title The title of the news.
     * @param summary The summary of the news.
     * @param content The content of the news.
     * @param isPublished Whether the news is published or not.
     */
    fun insertNews(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        viewModelScope.launch {
            val result = insertNewsUseCase.execute(title, summary, content, isPublished)
            if (result.isSuccess) {
                refreshRoomNews()
                loadLocalNews()
            }
        }
    }

    /**
     * Deletes a row from the news table with the provided ID.
     *
     * @param newsId The ID of the news to be deleted.
     */
    fun deleteNews(newsId: String) {
        viewModelScope.launch {
            val result = deleteNewsUseCase.deleteNews(newsId)
            if (result.isSuccess) {
                deleteNewsUseCase.deleteNews(newsId)
                refreshRoomNews()
                loadLocalNews()
            }
        }
    }

    /**
     * Save changes made to the currently selected item by updating it with new details
     *
     * @param title The new title of the news.
     * @param summary The new summary of the news.
     * @param content The new content of the news.
     * @param isPublished Whether the news is published or not.
     */
    fun saveNewsChanges(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        _selectedNews.value?.let { news ->
            updateNewsInDb(
                newsId = news.newsId,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
        }
    }

    /**
     * Update a news item in the database with new values. Logs the result console-side.
     *
     *
     * @param newsId The ID of the news to be updated.
     * @param title The new title of the news.
     * @param summary The new summary of the news.
     * @param content The new content of the news.
     * @param isPublished Whether the news is published or not.
     */
    private fun updateNewsInDb(
        newsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        viewModelScope.launch {
            val result = updateNewsUseCase.execute(
                newsId = newsId,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            if (result.isSuccess) {
                refreshRoomNews()
                loadLocalNews()
            }
        }
    }

    /**
     * Set the selected news-article to the provided value. Used for choosing what card to show in the details screen.
     */
    fun setSelectedNews(news: NewsData) {
        _selectedNews.value = news
    }

    /**
     * Clear the cards to reload the view in case of new data.
     *
     * TODO: REDUNDANT
     */
    fun clearSelectedNews() {
        _selectedNews.value = null
    }

    /**
     * Periodically refresh the news from the network. Used for automatic updates. Have put 2 min now. Its a pretty simple solution, but as it sits now
     * it only updates if the user is in the news screen for two minutes. Will push this further back in the code.
     *
     * UPDATE: I lied, but that might be a thing ^ It will still update if the user is in another screen, probably because all viewmodels are initiated.
     */
    private fun periodicRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(2 * 60 * 1000)
                refreshRoomNews()
                loadLocalNews()
            }
        }
    }
}
