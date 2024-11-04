package no.usn.mob3000.domain.viewmodel.content

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.repository.content.DocsRepository
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.domain.usecase.content.docs.DeleteDocsUseCase
import no.usn.mob3000.domain.usecase.content.docs.FetchDocUseCase
import no.usn.mob3000.domain.usecase.content.docs.InsertDocsUseCase
import no.usn.mob3000.domain.usecase.content.docs.UpdateDocsUseCase

/**
 * ViewModel for documentation.
 *
 * @author 258030
 * @contributor frigvid
 * @created 2024-11-04
 */
class DocumentationViewModel(
    private val fetchDocUseCase: FetchDocUseCase = FetchDocUseCase(),
    private val deleteDocsUseCase: DeleteDocsUseCase = DeleteDocsUseCase(),
    private val updateDocsUseCase: UpdateDocsUseCase = UpdateDocsUseCase(DocsRepository()),
    private val insertDocsUseCase: InsertDocsUseCase = InsertDocsUseCase(DocsRepository()),
): ViewModel() {
    private val _documentations =
        MutableStateFlow<Result<List<DocsData>>>(Result.success(emptyList()))
    val documentations: StateFlow<Result<List<DocsData>>> = _documentations

    private val _selectedDocumentation = mutableStateOf<DocsData?>(null)
    val selectedDocumentation: State<DocsData?> = _selectedDocumentation

    fun fetchDocumentations() {
        viewModelScope.launch {
            _documentations.value = fetchDocUseCase.fetchDocumentations()
        }
    }

    fun insertDocs(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String? = null
    ) {
        viewModelScope.launch {
            val result = insertDocsUseCase.execute(title, summary, content, isPublished, userId)
            if (result.isSuccess) {
                // TODO: Handle success
            } else {
                // TODO: Handle error
            }
        }
    }

    fun deleteDocs(docsId: String) {
        viewModelScope.launch {
            deleteDocsUseCase.deleteDocs(docsId)
        }
    }

    fun saveDocumentationChanges(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        _selectedDocumentation.value?.let { documentation ->
            val updatedDocumentation = documentation.copy(
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            updateDocumentationInDb(updatedDocumentation)
        }
    }

    private fun updateDocumentationInDb(documentation: DocsData) {
        viewModelScope.launch {
            val result = updateDocsUseCase.execute(
                docsId = documentation.docsId,
                title = documentation.title,
                summary = documentation.summary,
                content = documentation.content,
                isPublished = documentation.isPublished
            )
            if (result.isSuccess) {
                Log.d(
                    "ContentViewModel",
                    "Update successful for documentation with ID: ${documentation.docsId}"
                )
            } else {
                Log.e(
                    "ContentViewModel",
                    "Update failed for documentation with ID: ${documentation.docsId}"
                )
            }

        }
    }

    fun setSelectedDocumentation(documentation: DocsData) {
        _selectedDocumentation.value = documentation
    }

    fun clearSelectedDocumentation() {
        _selectedDocumentation.value = null
    }
}
