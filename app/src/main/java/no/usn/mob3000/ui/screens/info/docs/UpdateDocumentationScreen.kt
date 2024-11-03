package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.runtime.*
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to modify documentation.
 *
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun UpdateDocumentationScreen(

    viewModel: ContentViewModel,
    onSaveDocumentationClick: () -> Unit
) {
    val selectedDocumentation = viewModel.selectedDocumentation.value
    var title by remember { mutableStateOf(selectedDocumentation?.title ?: "") }
    var summary by remember { mutableStateOf(selectedDocumentation?.summary ?: "") }
    var content by remember { mutableStateOf(selectedDocumentation?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedDocumentation?.isPublished ?: true) }

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
            viewModel.saveDocumentationChanges(title, summary, content, isPublished)
            onSaveDocumentationClick()
        }
    )
}


