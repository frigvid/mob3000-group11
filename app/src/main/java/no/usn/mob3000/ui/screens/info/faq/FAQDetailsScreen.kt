package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ConfirmationDialog
import no.usn.mob3000.ui.components.info.ContentDisplay
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin

/**
 * Screen to display full details about some documentation.
 *
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun FAQDetailsScreen(
    setSelectedFaq: (FAQData) -> Unit,
    deleteFaqItem: (String) -> Unit,
    selectedFaq: FAQData? = null,
    navigateToFaqUpdate: () -> Unit,
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
                auth.isAdmin && selectedFaq?.isPublished == true ->
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
            if (selectedFaq != null) {
                deleteFaqItem(selectedFaq.faqId)
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
                                selectedFaq?.let {
                                    setSelectedFaq(it)
                                    navigateToFaqUpdate()
                                }
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit FAQ"
                                )
                            }
                            IconButton(onClick = { showConfirmationDialog.value = true }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete FAQ",
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
        if (selectedFaq != null) {
            ContentDisplay(
                modifier = Modifier.padding(innerPadding),
                title = selectedFaq.title,
                summary = selectedFaq.summary,
                content = selectedFaq.content,
                createdAt = selectedFaq.createdAt.toEpochMilliseconds(),
                modifiedAt = selectedFaq.modifiedAt.toEpochMilliseconds(),
                isPublishedText = isPublishedText
            )
        } else {
            Text(stringResource(R.string.documentation_details_not_found))
        }
    }
}
