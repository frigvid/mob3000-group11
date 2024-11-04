package no.usn.mob3000.domain.viewmodel.content

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.repository.content.FAQRepository
import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.domain.usecase.content.faq.DeleteFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.FetchFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.InsertFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.UpdateFAQUseCase

/**
 * ViewModel for FAQ.
 *
 * @author 258030
 * @contributor frigvid
 * @created 2024-11-04
 */
class FAQViewModel(
    private val fetchFAQUseCase: FetchFAQUseCase = FetchFAQUseCase(),
    private val deleteFAQUseCase: DeleteFAQUseCase = DeleteFAQUseCase(),
    private val updateFAQUseCase: UpdateFAQUseCase = UpdateFAQUseCase(FAQRepository()),
    private val insertFAQUseCase: InsertFAQUseCase = InsertFAQUseCase(FAQRepository())
): ViewModel() {
    private val _faq = MutableStateFlow<Result<List<FAQData>>>(Result.success(emptyList()))
    val faq: StateFlow<Result<List<FAQData>>> = _faq

    private val _selectedFAQ = mutableStateOf<FAQData?>(null)
    val selectedFAQ: State<FAQData?> = _selectedFAQ

    fun fetchFAQ() {
        viewModelScope.launch {
            _faq.value = fetchFAQUseCase.fetchFAQ()
        }
    }

    fun insertFAQ(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String? = null
    ) {
        viewModelScope.launch {
            val result = insertFAQUseCase.execute(title, summary, content, isPublished, userId)
            if (result.isSuccess) {
                // TODO: Handle success
            } else {
                // TODO: Handle error
            }
        }
    }

    fun deleteFAQ(faqId: String) {
        viewModelScope.launch {
            deleteFAQUseCase.deleteFAQ(faqId)
        }
    }

    fun saveFAQChanges(
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

    fun setSelectedFAQ(faq: FAQData) {
        _selectedFAQ.value = faq
    }

    fun clearSelectedFAQ() {
        _selectedFAQ.value = null
    }
}
