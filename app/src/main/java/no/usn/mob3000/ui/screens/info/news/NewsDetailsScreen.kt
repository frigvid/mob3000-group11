package no.usn.mob3000.ui.screens.info.news

import ConfirmationDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.ui.components.info.ContentDisplay

/**
 * Screen to display full details about some documentation.
 *
 * This screen shows the complete content of a news article, including its title, summary, content,
 * creation date, modification date, and publication status. It also provides an edit option for
 * authorized users.
 *
 * @param selectedNews The [NewsData] object to display.
 * @param onEditClick Callback function to navigate to the edit screen.
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun NewsDetailsScreen(
    setSelectedNews: (NewsData) -> Unit,
    deleteNewsItem: (String) -> Unit,
    selectedNews: NewsData? = null,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val showConfirmationDialog = remember { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        onConfirm = {
            if (selectedNews != null) {
                deleteNewsItem(selectedNews.newsId)
                onDeleteClick()
            }
        }
    )

    Viewport(
        topBarActions = {
            Row {
                IconButton(onClick = {
                    selectedNews?.let {
                        setSelectedNews(it)
                        onEditClick()
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
    ) { innerPadding ->
        if (selectedNews != null) {
            ContentDisplay(
                modifier = Modifier.padding(innerPadding),
                title = selectedNews.title,
                summary = selectedNews.summary,
                content = selectedNews.content,
                createdAt = selectedNews.createdAt.toEpochMilliseconds(),
                modifiedAt = selectedNews.modifiedAt.toEpochMilliseconds(),
                isPublished = selectedNews.isPublished
            )

        } else {
            Text(stringResource(R.string.news_details_not_found))
        }
    }
}
