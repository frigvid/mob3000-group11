package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import java.util.Date

/**
 * Screen to create or modify FAQ items.
 *
 * @param selectedFAQ The [FAQ] object from ViewModel state.
 * @param onSaveFAQClick Callback function to navigate to [FAQScreen].
 * @param onDeleteFAQClick Callback function to navigate to [FAQScreen].
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun CreateFAQScreen(
    selectedFAQ: FAQ?,
    onSaveFAQClick: (FAQ) -> Unit,
    onDeleteFAQClick: (String) -> Unit
) {
    var title by remember { mutableStateOf(selectedFAQ?.title ?: "") }
    var subtitle by remember { mutableStateOf(selectedFAQ?.subtitle ?: "") }
    var content by remember { mutableStateOf(selectedFAQ?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedFAQ?.isPublished ?: true) }

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
                label = { Text(stringResource(R.string.faq_create_label_title)) },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text(stringResource(R.string.faq_create_label_subtitle)) },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.faq_create_label_content) ) },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPublished,
                    onCheckedChange = { isPublished = it }
                )

                Text(
                    if (selectedFAQ == null) stringResource(R.string.faq_create_check_publish)
                    else stringResource(R.string.faq_create_check_unpublish)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val newFAQ = FAQ(
                            id = selectedFAQ?.id ?: "",
                            createdAt = selectedFAQ?.createdAt ?: Date(),
                            modifiedAt = Date(),
                            createdBy = selectedFAQ?.createdBy ?: "",
                            title = title,
                            subtitle = subtitle,
                            content = content,
                            isPublished = isPublished
                        )
                        onSaveFAQClick(newFAQ)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(
                        if (selectedFAQ == null) stringResource(R.string.faq_create_save_faq)
                        else stringResource(R.string.faq_create_save_changes)
                    )
                }

                if (selectedFAQ != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { onDeleteFAQClick(selectedFAQ.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text(stringResource(R.string.faq_create_delete_faq)) }
                }
            }
        }
    }
}
