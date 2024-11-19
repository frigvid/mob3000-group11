package no.usn.mob3000.ui.screens.info.faq

import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn

/**
 * Screen for the FAQ page, with a list of FAQ articles.
 *
 * @author frigvid, 258030
 * @created 2024-09-23
 */
@Composable
fun FAQScreen(
    faqState: StateFlow<Result<List<FAQData>>>,
    fetchFaq: () -> Unit,
    onFaqClick: (FAQData) -> Unit,
    onCreateFAQClick: () -> Unit,
    setSelectedFAQ: (FAQData) -> Unit,
    clearSelectedFAQ: () -> Unit,
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit
) {
    val faqResult by faqState.collectAsState()

    /**
     * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-11
     */
    val state by remember { authenticationState }.collectAsState()

    LaunchedEffect(Unit) {
        authenticationStateUpdate()
        clearSelectedFAQ()
        fetchFaq()
    }

    Viewport(
        floatingActionButton = {
            when (val auth = state) {
                is AuthenticationState.Authenticated -> {
                    if (auth.isAdmin) {
                        FloatingActionButton(
                            onClick = onCreateFAQClick,
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                            Icons.Filled.Add,
                            contentDescription = "Create FAQ",
                            tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                else -> return@Viewport
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
                        authenticationStateUpdate()
                        onFaqClick(faqItem)
                    }
                )
            }
        }
    }
}
