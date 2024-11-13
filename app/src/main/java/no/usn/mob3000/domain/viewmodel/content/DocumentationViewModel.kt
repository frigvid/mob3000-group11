package no.usn.mob3000.domain.viewmodel.content

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.repository.content.remote.DocsRepository
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.domain.usecase.content.docs.DeleteDocsUseCase
import no.usn.mob3000.domain.usecase.content.docs.FetchDocUseCase
import no.usn.mob3000.domain.usecase.content.docs.InsertDocsUseCase
import no.usn.mob3000.domain.usecase.content.docs.UpdateDocsUseCase

/**
 * ViewModel for documentation. Using the different usecases to communicate with the business logic handled in
 * the data layer.
 *
 * @param fetchDocUseCase The use case for fetching documentation.
 * @param deleteDocsUseCase The use case for deleting documentation.
 * @param updateDocsUseCase The use case for updating documentation.
 * @param insertDocsUseCase The use case for inserting documentation.
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

    /**
     * Fetches documentation from the data layer.
     */
    fun fetchDocumentations() {
        viewModelScope.launch {
            _documentations.value = fetchDocUseCase.fetchDocumentations()
        }
    }

    /**
     * Inserts a new row into the docs table with the provided parameters.
     *
     * @param title The title of the documentation.
     * @param summary The summary of the documentation.
     * @param content The content of the documentation.
     * @param isPublished Whether the documentation is published or not.
     */
    fun insertDocs(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        viewModelScope.launch {
            val result = insertDocsUseCase.execute(title, summary, content, isPublished)
            if (result.isSuccess) {
                // TODO: Handle success
            } else {
                // TODO: Handle error
            }
        }
    }

    /**
     * Deletes a row from the docs table with the provided ID.
     *
     * @param docsId The ID of the documentation to be deleted.
     */
    fun deleteDocs(docsId: String) {
        viewModelScope.launch {
            deleteDocsUseCase.deleteDocs(docsId)
        }
    }

    /**
     * Save changes made to the currently selected item by updating it with new details
     *
     * @param title The new title of the documentation.
     * @param summary The new summary of the documentation.
     * @param content The new content of the documentation.
     * @param isPublished Whether the documentation is published or not.
     */
    fun saveDocumentationChanges(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        _selectedDocumentation.value?.let { documentation ->
            updateDocumentationInDb(
                docsId = documentation.docsId,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
        }
    }

    /**
     * Update a docs item in the database with new values. Logs the result console-side.
     *
     * TODO: Remove logs before final iteration
     *
     * @param docsId The ID of the documentation to be updated.
     * @param title The new title of the documentation.
     * @param summary The new summary of the documentation.
     * @param content The new content of the documentation.
     * @param isPublished Whether the documentation is published or not.
     */
    private fun updateDocumentationInDb(
        docsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ) {
        viewModelScope.launch {
            val result = updateDocsUseCase.execute(
                docsId = docsId,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )
            if (result.isSuccess) {
                Log.d(
                    "ContentViewModel",
                    "Update successful for documentation with ID: $docsId"
                )
            } else {
                Log.e(
                    "ContentViewModel",
                    "Update failed for documentation with ID: $docsId"
                )
            }

        }
    }

    /**
     * Set the selected documentation to the provided value. Used for choosing what card to show in the details screen.
     */
    fun setSelectedDocumentation(documentation: DocsData) {
        _selectedDocumentation.value = documentation
    }

    /**
     * Clear the cards to reload the view in case of new data.
     *
     * Todo: Localstorage and a listener to only update the cards when changes are made.
     */
    fun clearSelectedDocumentation() {
        _selectedDocumentation.value = null
    }
}
