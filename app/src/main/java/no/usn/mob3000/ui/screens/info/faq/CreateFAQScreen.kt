package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.model.content.FaqDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.ui.theme.DefaultButton
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
    selectedFAQ: FaqDto?,
    onSaveFAQClick: () -> Unit

) {
    var title by remember { mutableStateOf(selectedFAQ?.title ?: "") }
    var summary by remember { mutableStateOf(selectedFAQ?.summary ?: "") }
    var content by remember { mutableStateOf(selectedFAQ?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedFAQ?.isPublished ?: true) }

    val scope = rememberCoroutineScope()

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
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text(stringResource(R.string.faq_create_label_subtitle)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.faq_create_label_content)) },
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
                if (selectedFAQ == null) {
                    Text(stringResource(R.string.faq_create_check_publish))
                } else {
                    Text(stringResource(R.string.faq_create_check_unpublish) + "?")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        val currentUserId = DbUtilities().getCurrentUserId()

                        if (currentUserId != null) {
                            val faqItem = FaqDto(
                                faqId = null,
                                createdAt = Clock.System.now(),
                                modifiedAt = Clock.System.now(),
                                createdByUser = currentUserId,
                                title = title,
                                summary = summary,
                                content = content,
                                isPublished = isPublished
                            )

                            val result = DbUtilities().insertItem("faq", faqItem, FaqDto.serializer())
                            if (result.isSuccess) {
                                println("Fantastisk")
                                onSaveFAQClick()
                            } else {
                                println("Error publishing the FAQ: ${result.exceptionOrNull()?.message}")
                            }
                        } else {
                            println("Error: Current user ID is null.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(DefaultButton)
            ) {
                if (selectedFAQ == null) {
                    Text(stringResource(R.string.faq_create_save_changes))
                } else {
                    Text(stringResource(R.string.faq_create_delete_faq))
                }
            }
        }
    }
}
