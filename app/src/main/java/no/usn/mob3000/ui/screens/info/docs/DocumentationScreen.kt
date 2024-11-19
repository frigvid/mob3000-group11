package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin
import java.util.*

/**
 * Screen for the documentation page.
 *
 * @param docsState The documentation state.
 * @param fetchDocs Callback function to get the documentation.
 * @param onDocsClick Arbitrary unit function.
 * @param onCreateDocumentationClick Callback function to navigate to [CreateDocumentationScreen].
 * @param setSelectedDocumentation ViewModel function to store a specific [Documentation] object in state.
 * @param clearSelectedDocumentation ViewModel function to clear the stored state documentation object.
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update the authentication status state.
 * @author frigvid, 258030
 * @created 2024-09-23
 */
@Composable
fun DocumentationScreen(
    docsState: StateFlow<Result<List<DocsData>>>,
    fetchDocs: () -> Unit,
    onDocsClick: (DocsData) -> Unit,
    onCreateDocumentationClick: () -> Unit,
    setSelectedDocumentation: (DocsData) -> Unit,
    clearSelectedDocumentation: () -> Unit,
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit
) {
    val documentationResult by docsState.collectAsState()

    /**
     * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-11
     */
    val state by remember { authenticationState }.collectAsState()

    LaunchedEffect(Unit) {
        authenticationStateUpdate()
        clearSelectedDocumentation()
        fetchDocs()
    }

    /**
     * Using the FAB to create a new news article. Clicking will navigate to the [CreateDocumentationScreen].
     */
    Viewport(
        floatingActionButton = {
            when (state) {
                is AuthenticationState.Authenticated -> {
                    FloatingActionButton(
                        onClick = onCreateDocumentationClick,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Create Documentation",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                else -> return@Viewport
            }
        }
    ) { innerPadding ->
        /**
         * Lazy column to display the list of news articles. The column padding is the same for
         * all info-main screens. Abstracted to reduce redundancy.
         */
        PaddedLazyColumn(innerPadding = innerPadding) {
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
                         authenticationStateUpdate()
                         onDocsClick(docsItem)
                     }
                 )
             }
         }
    }
}
