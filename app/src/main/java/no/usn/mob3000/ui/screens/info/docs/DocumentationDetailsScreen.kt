package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ConfirmationDialog
import no.usn.mob3000.ui.components.info.ContentDisplay
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin
import java.util.*

/**
 * Screen to display full details about some documentation.
 *
 * @param selectedDocumentation The [Documentation] object to display.
 * @param navigateToDocumentationUpdate Callback function to navigate to [CreateDocumentationScreen].
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun DocumentationDetailsScreen(
    setSelectedDoc: (DocsData) -> Unit,
    deleteDocItem: (String) -> Unit,
    selectedDoc: DocsData? = null,
    navigateToDocumentationUpdate: () -> Unit,
    popNavigationBackStack: () -> Unit,
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit
) {
    val showConfirmationDialog = remember { mutableStateOf(false) }

    /**
     * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-11
     */
    val state by remember { authenticationState }.collectAsState()

    LaunchedEffect(Unit) { authenticationStateUpdate() }

    val isPublishedText = when (val auth = state) {
        is AuthenticationState.Authenticated -> {
            when {
                auth.isAdmin && selectedDoc?.isPublished == true ->
                    stringResource(R.string.info_item_details_status_published)
                auth.isAdmin ->
                    stringResource(R.string.info_item_details_status_draft)
                else -> ""
            }
        }
        else -> ""
    }

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        onConfirm = {
            if (selectedDoc != null) {
                deleteDocItem(selectedDoc.docsId)
                popNavigationBackStack()
            }
        }
    )

    Viewport(
        topBarActions = {
            when (val auth = state) {
                is AuthenticationState.Authenticated -> {
                    if (auth.isAdmin) {
                        Row {
                            IconButton(onClick = {
                                selectedDoc?.let {
                                    setSelectedDoc(it)
                                    navigateToDocumentationUpdate()
                                }
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit Documentation"
                                )
                            }
                            IconButton(onClick = { showConfirmationDialog.value = true }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete Documentation",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
                else -> return@Viewport
            }
        }
    ) { innerPadding ->
        if (selectedDoc != null) {
            ContentDisplay(
                modifier = Modifier.padding(innerPadding),
                title = selectedDoc.title,
                summary = selectedDoc.summary,
                content = selectedDoc.content,
                createdAt = selectedDoc.createdAt.toEpochMilliseconds(),
                modifiedAt = selectedDoc.modifiedAt.toEpochMilliseconds(),
                isPublishedText = isPublishedText
            )
        } else {
            Text(stringResource(R.string.documentation_details_not_found))
        }
    }
}
