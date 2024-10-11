package no.usn.mob3000.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import no.usn.mob3000.ui.screens.chess.train.opening.Opening
import no.usn.mob3000.ui.screens.info.News

/**
 * ViewModel to save state where necessary.
 *
 * @author frigvid
 * @created 2024-10-08
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

    /**
     *
     */

    private val _news = mutableStateOf<List<News>>(emptyList())
    val news: State<List<News>> = _news

    private val _selectedNews = mutableStateOf<News?>(null)
    val selectedNews: State<News?> = _selectedNews

    fun setNews(news: List<News>) {
        _news.value = news
    }

    fun setSelectedNews(news: News) {
        _selectedNews.value = news
    }

    fun getNewsById(id: String): News? {
        return _news.value.find { it.id == id }
    }


    /**
     * Retrieves an Opening by its ID.
     *
     * @param id The ID of the opening to retrieve.
     * @return The Opening with the matching ID, or null if not found.
     * @author frigvid
     * @created 2024-10-09
     */
    fun getOpeningById(id: String): Opening? {
        return _openings.value.find { it.id == id }
    }

    /* Settings.
     * 
     * TODO: Figure out a better way to store default values. Can't use composables here.
     */
    private val _selectedTheme = mutableStateOf("Default - light")
    val selectedTheme: State<String> = _selectedTheme

    private val _selectedLanguage = mutableStateOf("English")
    val selectedLanguage: State<String> = _selectedLanguage

    /* TODO: Add functionality to actually switch themes, somewhere. Probably not here though. */
    fun setSelectedTheme(theme: String) {
        _selectedTheme.value = theme
    }

    /* TODO: Add functionality to actually switch language, somewhere. Probably not here though. */
    fun setSelectedLanguage(language: String) {
        _selectedLanguage.value = language
    }

}