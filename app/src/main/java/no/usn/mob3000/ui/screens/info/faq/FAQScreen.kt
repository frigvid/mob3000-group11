package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn

/**
 * Screen for the FAQ page, with a list of FAQ articles.
 *
 * @author frigvid, 258030 (Eirik)
 * @created 2024-09-23
 */
@Composable
fun FAQScreen(
    faqState: StateFlow<Result<List<FAQData>>>,
    fetchFaq: () -> Unit,
    navigateToFaqDetails: (FAQData) -> Unit,
    onCreateFAQClick: () -> Unit,
    setSelectedFAQ: (FAQData) -> Unit,
    clearSelectedFAQ: () -> Unit
) {
    val faqResult by faqState.collectAsState()

    LaunchedEffect(Unit) {
        clearSelectedFAQ()
        fetchFaq()
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateFAQClick,
                containerColor = DefaultButton
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create FAQ")
            }
        }
    ) { innerPadding ->
        PaddedLazyColumn(innerPadding = innerPadding) {
            items(faqResult.getOrThrow()) { faqItem ->
                ContentItem(
                    title = faqItem.title,
                    summary = faqItem.summary,
                    isPublished = faqItem.isPublished,
                    onClick = {
                        setSelectedFAQ(faqItem)
                        navigateToFaqDetails(faqItem) }
                )
            }
        }
    }
}


