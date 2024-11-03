package no.usn.mob3000.ui.screens.info.docs

import android.util.Log
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
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentEditor
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen to create or modify documentation.
 *
 * TODO: Publish/unpublish should probably be somewhat better than this.
 *
 * @author frigvid, 258030 (Eirik)
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

