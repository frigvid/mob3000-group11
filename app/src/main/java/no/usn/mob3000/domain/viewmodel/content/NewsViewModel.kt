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
 * ViewModel for news.
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

    fun fetchNews() {
        viewModelScope.launch {
            _news.value = fetchNewsUseCase.fetchNews()
        }
    }

    fun insertNews(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String? = null
    ) {
        viewModelScope.launch {
            val result = insertNewsUseCase.execute(title, summary, content, isPublished, userId)
            if (result.isSuccess) {
                // TODO: Handle success
            } else {
                // TODO: Handle error
            }
        }
    }

    fun deleteNews(newsId: String) {
        viewModelScope.launch {
            deleteNewsUseCase.deleteNews(newsId)
        }
    }

    fun saveNewsChanges(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        _selectedNews.value?.let { news ->
            val updatedNews = news.copy(
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            updateNewsInDb(updatedNews)
        }
    }

    private fun updateNewsInDb(news: NewsData) {
        viewModelScope.launch {
            val result = updateNewsUseCase.execute(
                newsId = news.newsId,
                title = news.title,
                summary = news.summary,
                content = news.content,
                isPublished = news.isPublished
            )
            if (result.isSuccess) {
                Log.d("ContentViewModel", "Update successful for news with ID: ${news.newsId}")
            } else {
                Log.e("ContentViewModel", "Update failed for news with ID: ${news.newsId}")
            }
        }
    }

    fun setSelectedNews(news: NewsData) {
        _selectedNews.value = news
    }

    fun clearSelectedNews() {
        _selectedNews.value = null
    }
}
