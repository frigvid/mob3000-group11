package no.usn.mob3000.ui.screens.info.news

import ConfirmationDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentDisplay


/**
 * Screen to display full details about some documentation.
 *
 * This screen shows the complete content of a news article, including its title, summary, content,
 * creation date, modification date, and publication status. It also provides an edit option for
 * authorized users.
 *
 * @param selectedNews The [NewsDto] object to display.
 * @param onEditClick Callback function to navigate to the edit screen.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun NewsDetailsScreen(
    newsViewModel: ContentViewModel,
    newsId: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val selectedNews by newsViewModel.selectedNews
    var showConfirmationDialog = remember { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        onConfirm = onDeleteClick
    )

    Viewport(
        topBarActions = {
            Row {
                IconButton(onClick = {
                    selectedNews?.let {
                        newsViewModel.setSelectedNews(it)
                        onEditClick()
                    }
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit News")
                }
                IconButton(onClick = { showConfirmationDialog.value = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete News")
                }
            }
        }
    ) { innerPadding ->
        if (selectedNews != null) {
            ContentDisplay(
                title = selectedNews!!.title ?: "",
                summary = selectedNews!!.summary,
                content = selectedNews!!.content,
                modifier = Modifier.padding(innerPadding)
            )
                /**
                     * Had some formating problems, didnt have time to fuck around with this
                     * TODO: Replacing with something to show date correctly
                     */

                    //Text(
                    //    text = stringResource(R.string.news_details_created_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.news_details_created_date_pattern), Locale.getDefault()).format(selectedNews.created_at)}",
                    //    fontSize = 12.sp,
                    //    modifier = Modifier.padding(bottom = 4.dp)
                    // )

                    // Text(
                    //    text = stringResource(R.string.news_details_modified_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.news_details_modified_date_pattern), Locale.getDefault()).format(selectedNews.modified_at)}",
                    //    fontSize = 12.sp,
                    //    modifier = Modifier.padding(bottom = 4.dp)
                    // )

                    // Text(
                    //    text = stringResource(R.string.news_details_status_prefix) + ": ${if (selectedNews.is_published) stringResource(R.string.news_details_status_published) else stringResource(R.string.news_details_status_draft)}",
                    //    fontSize = 12.sp
                    // )


        } else {
            Text(stringResource(R.string.news_details_not_found))
        }
    }


}
