package no.usn.mob3000.ui.screens.info.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 * Screen to create or modify documentation.
 *
 * TODO: Publish/unpublish should probably be somewhat better than this.
 *
 * @param selectedNews The [News] object to display for editing, if any.
 * @param onSaveNewsClick Callback function to handle saving the news article.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun CreateNewsScreen(
    selectedNews: News?,
    onSaveNewsClick: () -> Unit
) {
    var title by remember { mutableStateOf(selectedNews?.title ?: "") }
    var summary by remember { mutableStateOf(selectedNews?.summary ?: "") }
    var content by remember { mutableStateOf(selectedNews?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedNews?.isPublished ?: true) }

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
                label = { Text(stringResource(R.string.news_create_label_title)) },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text(stringResource(R.string.news_create_label_summary)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                minLines = 3
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.news_create_label_content)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                minLines = 14
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPublished,
                    onCheckedChange = { isPublished = it }
                )

                if (selectedNews == null) {
                    Text(stringResource(R.string.news_create_check_publish))
                } else {
                    Text(stringResource(R.string.news_create_check_unpublish))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSaveNewsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                if (selectedNews == null) {
                    Text(stringResource(R.string.news_create_save_news))
                } else {
                    Text(stringResource(R.string.news_create_save_changes))
                }
            }
        }
    }
}
