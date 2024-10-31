package no.usn.mob3000.ui.screens.info.faq

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
import no.usn.mob3000.data.model.content.FaqDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen to modify update
 *
 * TODO: Publish/unpublish should probably be somewhat better than this.
 *
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun UpdateFAQScreen(

    selectedFAQ: FaqDto?,
    onSaveFAQClick: () -> Unit
) {
    var title by remember { mutableStateOf(selectedFAQ?.title ?: "") }
    var summary by remember { mutableStateOf(selectedFAQ?.summary ?: "") }
    var content by remember { mutableStateOf(selectedFAQ?.content ?: "") }
    var isPublished by remember { mutableStateOf(selectedFAQ?.isPublished ?: true) }

    val scope = rememberCoroutineScope()
    val dbUtilities = DbUtilities()

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
                label = { Text(stringResource(R.string.faq_update_title)) },
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

                if ( selectedFAQ == null) {
                    Text(stringResource(R.string.faq_create_check_publish))
                } else {
                    Text(stringResource(R.string.faq_create_check_unpublish))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        selectedFAQ?.faqId?.let { faqId ->
                            val updatedFAQ =  selectedFAQ.copy(
                                title = title,
                                summary = summary,
                                content = content,
                                isPublished = isPublished
                            )
                            val result = dbUtilities.updateItem("faq", faqId, updatedFAQ, FaqDto.serializer())
                            if (result.isSuccess) onSaveFAQClick()
                            else Log.e("UpdateFAQ", "Error updating FAQ", result.exceptionOrNull())
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(DefaultButton)
            ) {
                if (selectedFAQ == null) {
                    Text(stringResource(R.string.faq_create_save_faq))
                } else {
                    Text(stringResource(R.string.faq_create_save_changes))
                }
            }
        }
    }
}
