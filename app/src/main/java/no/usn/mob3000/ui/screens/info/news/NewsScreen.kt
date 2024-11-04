package no.usn.mob3000.ui.screens.info.news

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.viewmodel.LoginState
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn

/**
 * Screen for displaying a list of news articles.
 *
 * @param onNewsClick Callback function to handle news item clicks.
 * @param onCreateNewsClick Callback function to handle the creation of a new news article.
 * @param setSelectedNews Callback function to set the selected news item.
 * @param clearSelectedNews Callback function to clear the selected news item.
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun NewsScreen(
    newsState: StateFlow<Result<List<NewsData>>>,
    fetchNews: () -> Unit,
    onNewsClick: (NewsData) -> Unit,
    onCreateNewsClick: () -> Unit,
    setSelectedNews: (NewsData) -> Unit,
    clearSelectedNews: () -> Unit
) {
    val newsResult by newsState.collectAsState()

    LaunchedEffect(Unit) {
        clearSelectedNews()
        fetchNews()
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewsClick,
                containerColor = DefaultButton
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create News")
            }
        }
    ) { innerPadding ->
        PaddedLazyColumn(innerPadding = innerPadding)
        {
            /**
             * Generating the list of documentation items.
             */
            items(newsResult.getOrThrow()) { newsItem ->
                ContentItem(
                    title = newsItem.title,
                    summary = newsItem.summary,
                    isPublished = newsItem.isPublished,
                    onClick = {
                        setSelectedNews(newsItem)
                        onNewsClick(newsItem)
                    }
                )
            }
        }
    }
}
