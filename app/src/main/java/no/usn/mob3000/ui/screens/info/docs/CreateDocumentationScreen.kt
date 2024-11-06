package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen to create or modify documentation.
 *
 * TODO: Publish/unpublish should probably be somewhat better than this.
 *
 * @param selectedDocumentation The [Documentation] object to display, if any.
 * @param onSaveDocumentationClick Fallback function to navigate to [DocumentationScreen].
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun CreateDocumentationScreen(
    selectedDocumentation: Documentation?,
    onSaveDocumentationClick: () -> Unit
) {
    var title by remember { mutableStateOf(selectedDocumentation?.title ?: "") }
    var summary by remember { mutableStateOf(selectedDocumentation?.summary ?: "") }
    var content by remember { mutableStateOf(selectedDocumentation?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedDocumentation?.isPublished ?: true) }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.documentation_create_label_title)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text(stringResource(R.string.documentation_create_label_summary)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.documentation_create_label_content)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 14
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPublished,
                    onCheckedChange = { isPublished = it }
                )

                /* TODO: Implement functionality that actually allows this. */
                if (selectedDocumentation == null) {
                    Text(stringResource(R.string.documentation_create_check_publish))
                } else {
                    Text(stringResource(R.string.documentation_create_check_unpublish) + "?")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveDocumentationClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(DefaultButton)
            ) {
                if (selectedDocumentation == null) {
                    Text(stringResource(R.string.documentation_create_save_documentation))
                } else {
                    Text(stringResource(R.string.documentation_create_save_changes))
                }
            }
        }
    }
}
