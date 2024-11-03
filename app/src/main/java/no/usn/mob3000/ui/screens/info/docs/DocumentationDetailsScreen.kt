package no.usn.mob3000.ui.screens.info.docs

import ConfirmationDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentDisplay
import java.util.*

/**
 * Screen to display full details about some documentation.
 *
 * @param selectedDocumentation The [Documentation] object to display.
 * @param onEditClick Callback function to navigate to [CreateDocumentationScreen].
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun DocumentationDetailsScreen(
    docsViewModel: ContentViewModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val selectedDocumentation by docsViewModel.selectedDocumentation
    var showConfirmationDialog = remember { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        onConfirm = onDeleteClick
    )

    Viewport(
        topBarActions = {
            Row {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Documentation")
            }
                IconButton(onClick = { showConfirmationDialog.value = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Documentation")
                }
            }
        }
    ) { innerPadding ->
        if (selectedDocumentation != null) {
            ContentDisplay(
                title = selectedDocumentation!!.title ?: "",
                summary = selectedDocumentation!!.summary,
                content = selectedDocumentation!!.content,
                createdAt = selectedDocumentation?.createdAt?.toEpochMilliseconds(),
                modifiedAt = selectedDocumentation?.modifiedAt?.toEpochMilliseconds(),
                modifier = Modifier.padding(innerPadding)
            )

        } else {
            Text(stringResource(R.string.documentation_details_not_found))
        }
    }
}
