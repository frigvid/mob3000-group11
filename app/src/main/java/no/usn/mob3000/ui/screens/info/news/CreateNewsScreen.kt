package no.usn.mob3000.ui.screens.info.news

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to create or modify documentation.
 *
 * @param selectedNews The [News] object to display for editing, if any.
 * @param navControllerNavigateUp Callback function to handle saving the news article.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun CreateNewsScreen(
    insertNews: (String, String, String, Boolean) -> Unit,
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
            insertNews(title, summary, content, isPublished)
            navControllerNavigateUp()
        }
    )
}
