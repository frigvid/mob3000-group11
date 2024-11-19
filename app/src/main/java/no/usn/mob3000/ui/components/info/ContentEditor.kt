package no.usn.mob3000.ui.components.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport

/**
 * Content editor for news, documentation and faq.
 *
 * @author frigvid
 * @contributor 258030
 * @created 2024-10-30
 */
@Composable
fun ContentEditor(
    title: String,
    onTitleChange: (String) -> Unit,
    summary: String,
    onSummaryChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    isPublished: Boolean,
    onIsPublishedChange: (Boolean) -> Unit,
    onSaveClick: () -> Unit
) {
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
                onValueChange = onTitleChange,
                label = {
                    Text(
                        stringResource(R.string.info_item_content_title),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = summary,
                onValueChange = onSummaryChange,
                label = {
                    Text(
                        stringResource(R.string.info_item_content_summary),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                label = {
                    Text(
                        stringResource(R.string.info_item_content_content),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth(),
                minLines = 14
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isPublished,
                    onCheckedChange = onIsPublishedChange
                )
                Text(stringResource(R.string.info_item_content_publish))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.info_item_content_save))
            }
        }
    }
}
