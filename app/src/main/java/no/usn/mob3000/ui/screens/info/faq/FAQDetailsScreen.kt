package no.usn.mob3000.ui.screens.info.faq

import no.usn.mob3000.ui.components.info.ConfirmationDialog
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
import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.ui.components.info.ContentDisplay

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
    isAdmin: Boolean
) {
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val isPublishedText = when {
        isAdmin && selectedFaq?.isPublished == true -> stringResource(R.string.info_item_details_status_published)
        isAdmin -> stringResource(R.string.info_item_details_status_draft)
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
            if (isAdmin) {
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
