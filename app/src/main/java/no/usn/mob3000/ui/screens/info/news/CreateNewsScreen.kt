package no.usn.mob3000.ui.screens.info.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentEditor
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen to create or modify documentation.
 *
 *
 * @param selectedNews The [News] object to display for editing, if any.
 * @param onSaveNewsClick Callback function to handle saving the news article.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun CreateNewsScreen(
    viewModel: ContentViewModel,
    onSaveNewsClick: () -> Unit
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
            viewModel.insertNews(title, summary, content, isPublished)
            onSaveNewsClick()
        }
    )
}


