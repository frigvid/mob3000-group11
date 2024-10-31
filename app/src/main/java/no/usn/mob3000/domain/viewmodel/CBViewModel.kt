package no.usn.mob3000.domain.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.model.content.FaqDto
import no.usn.mob3000.ui.screens.chess.train.opening.Opening
import no.usn.mob3000.data.model.content.NewsDto

/**
 * ViewModel to save state where necessary.
 */
class CBViewModel : ViewModel() {
    /* Openings/Groups. */
    private val _openings = mutableStateOf<List<Opening>>(emptyList())
    val openings: State<List<Opening>> = _openings

    private val _selectedOpening = mutableStateOf<Opening?>(null)
    val selectedOpening: State<Opening?> = _selectedOpening

    fun setOpenings(openings: List<Opening>) {
        _openings.value = openings
    }

    fun setSelectedOpening(opening: Opening) {
        _selectedOpening.value = opening
    }

    fun getOpeningById(id: String): Opening? {
        return _openings.value.find { it.id == id }
    }

    /* Settings. */
    private val _selectedTheme = mutableStateOf("Default - light")
    val selectedTheme: State<String> = _selectedTheme

    private val _selectedLanguage = mutableStateOf("English")
    val selectedLanguage: State<String> = _selectedLanguage

    fun setSelectedTheme(theme: String) {
        _selectedTheme.value = theme
    }

    fun setSelectedLanguage(language: String) {
        _selectedLanguage.value = language
    }

    /* News. */
    private val _news = mutableStateOf<List<NewsDto>>(emptyList())
    val news: State<List<NewsDto>> = _news

    private val _selectedNews = mutableStateOf<NewsDto?>(null)
    val selectedNews: State<NewsDto?> = _selectedNews

    fun setNews(news: List<NewsDto>) {
        _news.value = news
    }

    fun setSelectedNews(news: NewsDto) {
        _selectedNews.value = news
    }

    fun clearSelectedNews() {
        _selectedNews.value = null
    }

    fun getNewsById(id: String): NewsDto? {
        return _news.value.find { it.newsId == id }
    }

    /* Documentation. */
    private val _documentations = mutableStateOf<List<DocsDto>>(emptyList())
    val documentations: State<List<DocsDto>> = _documentations

    private val _selectedDocumentation = mutableStateOf<DocsDto?>(null)
    val selectedDocumentation: State<DocsDto?> = _selectedDocumentation

    fun setDocumentations(documentations: List<DocsDto>) {
        _documentations.value = documentations
    }

    fun setSelectedDocumentation(documentation: DocsDto) {
        _selectedDocumentation.value = documentation
    }

    fun clearSelectedDocumentation() {
        _selectedDocumentation.value = null
    }

    /* FAQ */
    private val _faqs = mutableStateOf<List<FaqDto>>(emptyList())
    val faqs: State<List<FaqDto>> = _faqs

    private val _selectedFAQ = mutableStateOf<FaqDto?>(null)
    val selectedFAQ: State<FaqDto?> = _selectedFAQ

    fun setFAQs(faqs: List<FaqDto>) {
        _faqs.value = faqs
    }

    fun setSelectedFAQ(faq: FaqDto) {
        _selectedFAQ.value = faq
    }

    fun clearSelectedFAQ() {
        _selectedFAQ.value = null
    }
}
