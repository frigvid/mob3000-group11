package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.runtime.*
import no.usn.mob3000.Destination
import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to update a FAQ article.
 *
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun UpdateFAQScreen(
    selectedFaq: FAQData? = null,
    saveFaqChanges: (String, String, String, Boolean) -> Unit,
    navigateToFaq: () -> Unit
) {
    var title by remember { mutableStateOf(selectedFaq?.title ?: "") }
    var summary by remember { mutableStateOf(selectedFaq?.summary ?: "") }
    var content by remember { mutableStateOf(selectedFaq?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedFaq?.isPublished ?: true) }

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
            saveFaqChanges(title, summary, content, isPublished)
            navigateToFaq()
        }
    )
}
