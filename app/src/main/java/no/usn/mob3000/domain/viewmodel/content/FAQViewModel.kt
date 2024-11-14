package no.usn.mob3000.domain.viewmodel.content

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.domain.usecase.content.faq.DeleteFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.FetchFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.InsertFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.UpdateFAQUseCase

/**
 * ViewModel for FAQ. Using the different usecases to communicate with the business logic handled in
 * the data layer.
 *
 * @author 258030
 * @contributor frigvid
 * @created 2024-11-04
 */
class FAQViewModel(
    private val fetchFAQUseCase: FetchFAQUseCase,
    private val deleteFAQUseCase: DeleteFAQUseCase,
    private val updateFAQUseCase: UpdateFAQUseCase,
    private val insertFAQUseCase: InsertFAQUseCase
): ViewModel() {
    private var hasRefreshed = false


    private val _faq = MutableStateFlow<Result<List<FAQData>>>(Result.success(emptyList()))
    val faq: StateFlow<Result<List<FAQData>>> = _faq

    private val _selectedFAQ = mutableStateOf<FAQData?>(null)
    val selectedFAQ: State<FAQData?> = _selectedFAQ

    init {
        periodicRefresh()
    }

    /**
     * Fetches faq from the data layer.
     */
    fun fetchFAQ() {
        viewModelScope.launch {
            if (!hasRefreshed) {
                refreshRoomFAQ()
                hasRefreshed = true
            }
            loadLocalFAQ()
        }
    }


    suspend fun refreshRoomFAQ() {
        fetchFAQUseCase.refreshRoomFromNetwork()
    }

    suspend fun loadLocalFAQ() {
        _faq.value = fetchFAQUseCase.fetchLocalFaq()
    }


    /**
     * Inserts a new row into the faq table with the provided parameters.
     *
     * @param title The title of the FAQ.
     * @param summary The summary of the FAQ.
     * @param content The content of the FAQ.
     * @param isPublished Whether the FAQ is published or not.
     */
    fun insertFAQ(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
    ) {
        viewModelScope.launch {
            val result = insertFAQUseCase.execute(title, summary, content, isPublished)
            if (result.isSuccess) {
                refreshRoomFAQ()
                loadLocalFAQ()
            }
        }
    }

    /**
     * Deletes a row from the faq table with the provided ID.
     *
     * @param faqId The ID of the FAQ to be deleted.
     */
    fun deleteFAQ(faqId: String) {
        viewModelScope.launch {
            val result = deleteFAQUseCase.deleteFAQ(faqId)
            if (result.isSuccess) {
                deleteFAQUseCase.deleteFAQ(faqId)
                refreshRoomFAQ()
                loadLocalFAQ()
            }
        }
    }

    /**
     * Save changes made to the currently selected item by updating it with new details
     *
     * @param title The new title of the FAQ.
     * @param summary The new summary of the FAQ.
     * @param content The new content of the FAQ.
     * @param isPublished Whether the FAQ is published or not.
     */
    fun saveFAQChanges(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        _selectedFAQ.value?.let { faq ->
            updateFAQInDb(
                faqId = faq.faqId,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
        }
    }

    /**
     * Update a faq item in the database with new values. Logs the result console-side.
     *
     *
     * @param faqId The ID of the FAQ to be updated.
     * @param title The new title of the FAQ.
     * @param summary The new summary of the FAQ.
     * @param content The new content of the FAQ.
     * @param isPublished Whether the FAQ is published or not.
     */
    private fun updateFAQInDb(
        faqId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        viewModelScope.launch {
            val result = updateFAQUseCase.execute(
                faqId = faqId,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            if (result.isSuccess) {
                refreshRoomFAQ()
                loadLocalFAQ()
            }
        }
    }

    /**
     * Set the selected FAQ to the provided value. Used for choosing what card to show in the details screen.
     */
    fun setSelectedFAQ(faq: FAQData) {
        _selectedFAQ.value = faq
    }

    /**
     * Clear the cards to reload the view in case of new data.
     *
     * Todo: Redundant
     */
    fun clearSelectedFAQ() {
        _selectedFAQ.value = null
    }

    private fun periodicRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(2 * 60 * 1000)
                refreshRoomFAQ()
                loadLocalFAQ()
            }
        }
    }
}
