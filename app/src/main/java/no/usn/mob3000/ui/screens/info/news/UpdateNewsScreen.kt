package no.usn.mob3000.ui.screens.info.news

import androidx.compose.runtime.*
import no.usn.mob3000.domain.model.content.NewsData
import no.usn.mob3000.ui.components.info.ContentEditor

/**
 * Screen to update documentation.
 *
 * @param selectedNews The [NewsData] object to display for editing, if any.
 * @param saveNewsChanges Callback function to handle saving the news article.
 * @param navigateToNews Callback function to navigate to NEWS.
 * @author 258030
 * @contributor frigvid
 * @created 2024-10-30
 */
@Composable
fun UpdateNewsScreen(
    selectedNews: NewsData? = null,
    saveNewsChanges: (String, String, String, Boolean) -> Unit,
    navigateToNews: () -> Unit,
) {
    var title by remember { mutableStateOf(selectedNews?.title ?: "") }
    var summary by remember { mutableStateOf(selectedNews?.summary ?: "") }
    var content by remember { mutableStateOf(selectedNews?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedNews?.isPublished ?: true) }

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
            saveNewsChanges(title, summary, content, isPublished)
            navigateToNews()
        }
    )
}
