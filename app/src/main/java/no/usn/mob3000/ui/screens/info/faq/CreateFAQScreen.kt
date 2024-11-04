package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to create FAQ items.
 *
 * @param selectedFAQ The [FAQ] object from ViewModel state.
 * @param onSaveFAQClick Callback function to navigate to [FAQScreen].
 * @param onDeleteFAQClick Callback function to navigate to [FAQScreen].
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun CreateFAQScreen(
    insertFaq: (String, String, String, Boolean) -> Unit,
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
            insertFaq(title, summary, content, isPublished)
            navControllerNavigateUp()
        }
    )
}
