package no.usn.mob3000.ui.screens.info.faq

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
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentDisplay


/**
 * Screen to display full details about some documentation.
 *
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun FAQDetailsScreen(
    faqViewModel: ContentViewModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val selectedFAQ by faqViewModel.selectedFAQ
    var showConfirmationDialog = remember { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        onConfirm = onDeleteClick
    )

    Viewport(
        topBarActions = {
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit FAQ")
                }
                IconButton(onClick = { showConfirmationDialog.value = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete FAQ")
                }
            }
        }
    ) { innerPadding ->
        if (selectedFAQ != null) {
            ContentDisplay(
                title = selectedFAQ!!.title ?: "",
                summary = selectedFAQ!!.summary,
                content = selectedFAQ!!.content,
                createdAt = selectedFAQ?.createdAt?.toEpochMilliseconds(),
                modifiedAt = selectedFAQ?.modifiedAt?.toEpochMilliseconds(),
                modifier = Modifier.padding(innerPadding)
            )
//
//            /* TODO: Only display this for admins. */
//                    Text(
//                        text = stringResource(R.string.documentation_details_status_prefix) + "Status: ${if (selectedFAQ!!.isPublished) stringResource(R.string.documentation_details_status_published) else stringResource(R.string.documentation_details_status_draft)}",
//                      fontSize = 12.sp
//                      )


        } else {
            Text(stringResource(R.string.documentation_details_not_found))
        }
    }
}