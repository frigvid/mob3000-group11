package no.usn.mob3000.ui.screens.info.docs

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
    selectedDocumentation: DocsDto?,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dbUtilities = DbUtilities()
    val coroutineScope = rememberCoroutineScope()
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(stringResource(R.string.documentation_details_confirm))},
            text = { Text(stringResource(R.string.documentation_details_delete_text)) },
            confirmButton = {
                TextButton(onClick = {
                    selectedDocumentation?.docId?.let { docId ->
                        coroutineScope.launch {
                            val result = dbUtilities.deleteItem("docs", "id", docId)
                            result.onSuccess { onDeleteClick() }
                                .onFailure { error -> Log.e("DeleteDocs", "Failed to delete documentation", error) }
                        }
                    }
                    showConfirmationDialog = false
                }) {
                    Text(stringResource(R.string.documentation_details_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text(stringResource(R.string.documentation_details_delete_cancel))
                }
            }
        )
    }

    Viewport(
        topBarActions = {
            Row {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Documentation")
            }
                IconButton(onClick = { showConfirmationDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Documentation")
                }
            }
        }
    ) { innerPadding ->
        if (selectedDocumentation != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = selectedDocumentation.title!!,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    selectedDocumentation.summary?.let {
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    selectedDocumentation.content?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0xFFEFEBE9))
                        .padding(16.dp)
                ) {
                   // Text(
                    //    text = stringResource(R.string.documentation_details_created_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.documentation_details_created_date_pattern), Locale.getDefault()).format(selectedDocumentation.createdAt)}",
                      //  fontSize = 12.sp,
                       // modifier = Modifier.padding(bottom = 4.dp)
                    //)

                    //Text(
                      //  text = stringResource(R.string.documentation_details_modified_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.documentation_details_modified_date_pattern), Locale.getDefault()).format(selectedDocumentation.modifiedAt)}",
                       // fontSize = 12.sp,
                        //modifier = Modifier.padding(bottom = 4.dp)
                    //)

                    /* TODO: Only display this for admins. */
                    //Text(
                    //    text = stringResource(R.string.documentation_details_status_prefix) + "Status: ${if (selectedDocumentation.isPublished) stringResource(R.string.documentation_details_status_published) else stringResource(R.string.documentation_details_status_draft)}",
                      //  fontSize = 12.sp
                 //   )
                }
            }
        } else {
            Text(stringResource(R.string.documentation_details_not_found))
        }
    }
}
