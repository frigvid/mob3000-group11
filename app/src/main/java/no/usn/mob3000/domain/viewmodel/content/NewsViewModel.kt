package no.usn.mob3000.domain.viewmodel.content

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.repository.content.NewsRepository
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
    private val fetchNewsUseCase: FetchNewsUseCase = FetchNewsUseCase(),
    private val deleteNewsUseCase: DeleteNewsUseCase = DeleteNewsUseCase(),
    private val updateNewsUseCase: UpdateNewsUseCase = UpdateNewsUseCase(NewsRepository()),
    private val insertNewsUseCase: InsertNewsUseCase = InsertNewsUseCase(NewsRepository()),
): ViewModel() {
    private val _news = MutableStateFlow<Result<List<NewsData>>>(Result.success(emptyList()))
    val news: StateFlow<Result<List<NewsData>>> = _news

    private val _selectedNews = mutableStateOf<NewsData?>(null)
    val selectedNews: State<NewsData?> = _selectedNews

    /**
     * Fetches news from the data layer.
     */
    fun fetchNews() {
        viewModelScope.launch {
            _news.value = fetchNewsUseCase.fetchNews()
        }
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
                // TODO: Handle success
            } else {
                // TODO: Handle error
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
            deleteNewsUseCase.deleteNews(newsId)
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
     * Todo: Remove logs before final iteration
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
                Log.d("ContentViewModel", "Update successful for news with ID: $newsId")
            } else {
                Log.e("ContentViewModel", "Update failed for news with ID: $newsId")
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
     * Todo: Localstorage and a listener to only update the cards when changes are made.
     */
    fun clearSelectedNews() {
        _selectedNews.value = null
    }
}
