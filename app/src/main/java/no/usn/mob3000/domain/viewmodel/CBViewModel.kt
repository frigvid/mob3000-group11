package no.usn.mob3000.domain.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import no.usn.mob3000.ui.screens.chess.train.opening.Opening
import no.usn.mob3000.ui.screens.info.docs.Documentation
import no.usn.mob3000.ui.screens.info.faq.FAQ
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
    private val _news = mutableStateOf<List<NewsDto>>(emptyList()) // Endret fra News til NewsDto
    val news: State<List<NewsDto>> = _news

    private val _selectedNews = mutableStateOf<NewsDto?>(null) // Endret fra News til NewsDto
    val selectedNews: State<NewsDto?> = _selectedNews

    fun setNews(news: List<NewsDto>) { // Endret fra List<News> til List<NewsDto>
        _news.value = news
    }

    fun setSelectedNews(news: NewsDto) { // Endret fra News til NewsDto
        _selectedNews.value = news
    }

    fun clearSelectedNews() {
        _selectedNews.value = null
    }

    fun getNewsById(id: String): NewsDto? { // Endret fra News til NewsDto
        return _news.value.find { it.newsId == id }
    }

    /* Documentation. */
    private val _documentations = mutableStateOf<List<Documentation>>(emptyList())
    val documentations: State<List<Documentation>> = _documentations

    private val _selectedDocumentation = mutableStateOf<Documentation?>(null)
    val selectedDocumentation: State<Documentation?> = _selectedDocumentation

    fun setDocumentations(documentations: List<Documentation>) {
        _documentations.value = documentations
    }

    fun setSelectedDocumentation(documentation: Documentation) {
        _selectedDocumentation.value = documentation
    }

    fun clearSelectedDocumentation() {
        _selectedDocumentation.value = null
    }

    /* FAQ */
    private val _faqs = mutableStateOf<List<FAQ>>(emptyList())
    val faqs: State<List<FAQ>> = _faqs

    private val _selectedFAQ = mutableStateOf<FAQ?>(null)
    val selectedFAQ: State<FAQ?> = _selectedFAQ

    fun setFAQs(faqs: List<FAQ>) {
        _faqs.value = faqs
    }

    fun setSelectedFAQ(faq: FAQ) {
        _selectedFAQ.value = faq
    }

    fun clearSelectedFAQ() {
        _selectedFAQ.value = null
    }
}
