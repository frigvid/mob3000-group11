package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.runtime.*
import no.usn.mob3000.domain.model.DocsData
import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to modify documentation.
 *
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun UpdateDocumentationScreen(
    selectedDoc: DocsData? = null,
    saveDocChanges: (String, String, String, Boolean) -> Unit,
    navigateToDocs: () -> Unit
) {
    var title by remember { mutableStateOf(selectedDoc?.title ?: "") }
    var summary by remember { mutableStateOf(selectedDoc?.summary ?: "") }
    var content by remember { mutableStateOf(selectedDoc?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedDoc?.isPublished ?: true) }

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
            saveDocChanges(title, summary, content, isPublished)
            navigateToDocs()
        }
    )
}


