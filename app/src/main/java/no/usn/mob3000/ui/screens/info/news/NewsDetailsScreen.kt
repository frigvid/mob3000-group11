package no.usn.mob3000.ui.screens.info.news

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
import no.usn.mob3000.domain.model.content.NewsData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ConfirmationDialog
import no.usn.mob3000.ui.components.info.ContentDisplay
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin

/**
 * Screen to display full details about some documentation.
 *
 * This screen shows the complete content of a news article, including its title, summary, content,
 * creation date, modification date, and publication status. It also provides an edit option for
 * authorized users.
 *
 * @param selectedNews The [NewsData] object to display.
 * @param navigateToNewsUpdate Callback function to navigate to the edit screen.
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun NewsDetailsScreen(
    setSelectedNews: (NewsData) -> Unit,
    deleteNewsItem: (String) -> Unit,
    selectedNews: NewsData? = null,
    navigateToNewsUpdate: () -> Unit,
    navControllerPopBackStack: () -> Unit,
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
                auth.isAdmin && selectedNews?.isPublished == true ->
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
            if (selectedNews != null) {
                deleteNewsItem(selectedNews.newsId)
                navControllerPopBackStack()
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
                                selectedNews?.let {
                                    setSelectedNews(it)
                                    navigateToNewsUpdate()
                                }
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit News"
                                )
                            }

                            IconButton(onClick = { showConfirmationDialog.value = true }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete News",
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
        if (selectedNews != null) {
            ContentDisplay(
                modifier = Modifier.padding(innerPadding),
                title = selectedNews.title,
                summary = selectedNews.summary,
                content = selectedNews.content,
                createdAt = selectedNews.createdAt.toEpochMilliseconds(),
                modifiedAt = selectedNews.modifiedAt.toEpochMilliseconds(),
                isPublishedText = isPublishedText
            )

        } else {
            Text(stringResource(R.string.news_details_not_found))
        }
    }
}
