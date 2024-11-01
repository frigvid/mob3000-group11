package no.usn.mob3000.domain.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.repository.content.FAQRepository
import no.usn.mob3000.data.repository.content.NewsRepository
import no.usn.mob3000.domain.model.DocsData
import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.model.NewsUpdateData
import no.usn.mob3000.domain.usecase.content.DeleteDocsUseCase
import no.usn.mob3000.domain.usecase.content.DeleteFAQUseCase
import no.usn.mob3000.domain.usecase.content.DeleteNewsUseCase
import no.usn.mob3000.domain.usecase.content.FetchDocUseCase
import no.usn.mob3000.domain.usecase.content.FetchFAQUseCase
import no.usn.mob3000.domain.usecase.content.FetchNewsUseCase
import no.usn.mob3000.domain.usecase.content.UpdateFAQUseCase
import no.usn.mob3000.domain.usecase.content.UpdateNewsUseCase

class ContentViewModel(
    private val fetchDocUseCase: FetchDocUseCase = FetchDocUseCase(),
    private val fetchNewsUseCase: FetchNewsUseCase = FetchNewsUseCase(),
    private val fetchFAQUseCase: FetchFAQUseCase = FetchFAQUseCase(),
    private val deleteNewsUseCase: DeleteNewsUseCase = DeleteNewsUseCase(),
    private val deleteFAQUseCase: DeleteFAQUseCase = DeleteFAQUseCase(),
    private val deleteDocsUseCase: DeleteDocsUseCase = DeleteDocsUseCase(),
    private val updateNewsUseCase: UpdateNewsUseCase = UpdateNewsUseCase(NewsRepository()),
    private val updateFAQUseCase: UpdateFAQUseCase = UpdateFAQUseCase(FAQRepository())
) : ViewModel() {

    private val _documentations = MutableStateFlow<Result<List<DocsData>>>(Result.success(emptyList()))
    val documentations: StateFlow<Result<List<DocsData>>> = _documentations
    private val _selectedDocumentation = mutableStateOf<DocsData?>(null)
    val selectedDocumentation: State<DocsData?> = _selectedDocumentation

    private val _news = MutableStateFlow<Result<List<NewsData>>>(Result.success(emptyList()))
    val news: StateFlow<Result<List<NewsData>>> = _news
    private val _selectedNews = mutableStateOf<NewsData?>(null)
    val selectedNews: State<NewsData?> = _selectedNews


    private val _faq = MutableStateFlow<Result<List<FAQData>>>(Result.success(emptyList()))
    val faq: StateFlow<Result<List<FAQData>>> = _faq
    private val _selectedFAQ = mutableStateOf<FAQData?>(null)
    val selectedFAQ: State<FAQData?> = _selectedFAQ


    fun fetchDocumentations() {
        viewModelScope.launch {
            _documentations.value = fetchDocUseCase.fetchDocumentations()
        }
    }
    fun deleteDocs(docsId: String) {
        viewModelScope.launch {
            deleteDocsUseCase.deleteDocs(docsId)
        }
    }

    fun fetchNews() {
        viewModelScope.launch {
            _news.value = fetchNewsUseCase.fetchNews()
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


    fun fetchFAQ() {
        viewModelScope.launch {
            _faq.value = fetchFAQUseCase.fetchFAQ()
        }
    }
    fun deleteFAQ(faqId: String) {
        viewModelScope.launch {
            deleteFAQUseCase.deleteFAQ(faqId)
        }
    }

    fun saveFAQChanges (
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        _selectedFAQ.value?.let { faq ->
            val updatedFAQ = faq.copy(
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            updateFAQInDb(updatedFAQ)
        }

    }

    private fun updateFAQInDb(faq: FAQData) {
        viewModelScope.launch {
            val result = updateFAQUseCase.execute(
                faqId = faq.faqId,
                title = faq.title,
                summary = faq.summary,
                content = faq.content,
                isPublished = faq.isPublished
            )
            if (result.isSuccess) {
                Log.d("ContentViewModel", "Update successful for FAQ with ID: ${faq.faqId}")
            } else {
                Log.e("ContentViewModel", "Update failed for FAQ with ID: ${faq.faqId}")
            }
        }
    }



    // *******************DOC*********************

    fun setSelectedDocumentation(documentation: DocsData) {
        _selectedDocumentation.value = documentation
    }

    fun clearSelectedDocumentation() {
        _selectedDocumentation.value = null
    }
    // *******************NEWS*********************

    fun setSelectedNews(news: NewsData) {
        _selectedNews.value = news
    }

    fun clearSelectedNews() {
        _selectedNews.value = null
    }



    // *******************FAQ*********************

    fun setSelectedFAQ(faq: FAQData) {
        _selectedFAQ.value = faq
    }

    fun clearSelectedFAQ() {
        _selectedFAQ.value = null
    }





}
