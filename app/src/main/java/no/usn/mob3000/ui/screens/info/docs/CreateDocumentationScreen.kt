package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to create  documentation.
 *
 *
 * @param selectedDocumentation The [Documentation] object to display, if any.
 * @param onSaveDocumentationClick Fallback function to navigate to [DocumentationScreen].
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun CreateDocumentationScreen(
    insertDocumentation: (String, String, String, Boolean) -> Unit,
    navControllerNavigateUp: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isPublished by remember { mutableStateOf(true) }


    ContentEditor(
        title = title,
        onTitleChange = { title = it },
        summary = summary,
        onSummaryChange = { summary = it },
        content = content,
        onContentChange = { content = it },
        isPublished = isPublished,
        onIsPublishedChange = { isPublished = it },
        onSaveClick = {
            insertDocumentation(title, summary, content, isPublished)
            navControllerNavigateUp()
        }
    )
}
