package no.usn.mob3000.ui.screens.info.news

import androidx.compose.runtime.*
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentEditor

@Composable
fun UpdateNewsScreen(
    viewModel: ContentViewModel,
    onSaveNewsClick: () -> Unit
) {
    val selectedNews = viewModel.selectedNews.value
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
            viewModel.saveNewsChanges(title, summary, content, isPublished)
            onSaveNewsClick()
        }
    )
}
