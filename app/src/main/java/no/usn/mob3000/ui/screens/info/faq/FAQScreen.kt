package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn
import no.usn.mob3000.ui.screens.info.news.NewsScreen


/**
 * Screen for the documentation page.
 *
 * TODO: This, [FAQScreen] and [NewsScreen] can probably be made more generic, so they depend
 *       on fellow components similar to the web-version.
 *
 *
 * @author frigvid, 258030 (Eirik)
 * @created 2024-09-23
 */
@Composable
fun FAQScreen(
    faqViewModel: ContentViewModel,
    onFAQClick: (FAQData) -> Unit,
    onCreateFAQClick: () -> Unit,
    setSelectedFAQ: (FAQData) -> Unit,
    clearSelectedFAQ: () -> Unit
) {
    val faqResult by faqViewModel.faq.collectAsState()

    LaunchedEffect(Unit) {
        clearSelectedFAQ()
        faqViewModel.fetchFAQ()
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
                    title = faqItem.title ?: "",
                    summary = faqItem.summary,
                    isPublished = faqItem.isPublished,
                    onClick = {
                        setSelectedFAQ(faqItem)
                        onFAQClick(faqItem) }
                )
            }
        }
    }
}


