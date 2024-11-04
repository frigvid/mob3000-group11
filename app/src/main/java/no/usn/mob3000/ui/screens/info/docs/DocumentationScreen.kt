package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn
import java.util.*

/**
 * Screen for the documentation page.

 * @param documentations The list of documentation stored in the ViewModel's state.
 * @param navigateToDocumentationDetails Callback function to navigate to the [Documentation] object's [DocumentationDetailsScreen].
 * @param onCreateDocumentationClick Callback function to navigate to [CreateDocumentationScreen].
 * @param setDocumentationList ViewModel function to store the list of [Documentation] objects in state.
 * @param setSelectedDocumentation ViewModel function to store a specific [Documentation] object in state.
 * @param clearSelectedDocumentation ViewModel function to clear the stored state documentation object.
 * @author frigvid, 258030
 * @created 2024-09-23
 */
@Composable
fun DocumentationScreen(
    docsState: StateFlow<Result<List<DocsData>>>,
    fetchDocs: () -> Unit,
    navigateToDocumentationDetails: (DocsData) -> Unit,
    onCreateDocumentationClick: () -> Unit,
    setSelectedDocumentation: (DocsData) -> Unit,
    clearSelectedDocumentation: () -> Unit
) {
    val documentationResult by docsState.collectAsState()

    LaunchedEffect(Unit) {
        clearSelectedDocumentation()
        fetchDocs()
    }

    /**
     * Using the FAB to create a new news article. Clicking will navigate to the [CreateDocumentationScreen].
     */
    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateDocumentationClick,
                containerColor = DefaultButton
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create Documentation")
            }
        }
    ) { innerPadding ->
        /**
         * Lazy column to display the list of news articles. The column padding is the same for
         * all info-main screens. Abstracted to reduce redundancy.
         */
        PaddedLazyColumn(innerPadding = innerPadding)
         {
             /**
              * Generating the list of documentation items.
              */
             items(documentationResult.getOrThrow()) { docsItem ->
                 ContentItem(
                     title = docsItem.title,
                     summary = docsItem.summary,
                     isPublished = docsItem.isPublished,
                     onClick = {
                         setSelectedDocumentation(docsItem)
                         navigateToDocumentationDetails(docsItem) }
                 )
             }
         }
    }
}
