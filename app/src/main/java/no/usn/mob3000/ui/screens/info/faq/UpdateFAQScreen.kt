package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.runtime.*
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to update a FAQ article.
 *
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun UpdateFAQScreen(
    viewModel: ContentViewModel,
    onSaveFAQClick: () -> Unit
) {
    val selectedFAQ = viewModel.selectedFAQ.value
    var title by remember { mutableStateOf(selectedFAQ?.title ?: "") }
    var summary by remember { mutableStateOf(selectedFAQ?.summary ?: "") }
    var content by remember { mutableStateOf(selectedFAQ?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedFAQ?.isPublished ?: true) }

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
            viewModel.saveFAQChanges(title, summary, content, isPublished)
            onSaveFAQClick()
        }
    )
}
