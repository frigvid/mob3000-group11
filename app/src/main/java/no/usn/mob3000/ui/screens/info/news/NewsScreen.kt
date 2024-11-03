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
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.viewmodel.ContentViewModel
import no.usn.mob3000.ui.components.info.ContentItem
import no.usn.mob3000.ui.components.info.PaddedLazyColumn


/**
 * Screen for displaying a list of news articles.
 *
 * @param newsViewModel The [ContentViewModel] instance to fetch news data.
 * @param onNewsClick Callback function to handle news item clicks.
 * @param onCreateNewsClick Callback function to handle the creation of a new news article.
 * @param setSelectedNews Callback function to set the selected news item.
 * @param clearSelectedNews Callback function to clear the selected news item.
 * @author frigvid, 258030
 * @created 2024-10-12
 */
@Composable
fun NewsScreen(
    newsViewModel: ContentViewModel,
    onNewsClick: (NewsData) -> Unit,
    onCreateNewsClick: () -> Unit,
    setSelectedNews: (NewsData) -> Unit,
    clearSelectedNews: () -> Unit
) {
    val newsResult by newsViewModel.news.collectAsState()

    LaunchedEffect(Unit) {
        clearSelectedNews()
        newsViewModel.fetchNews()
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
             * Generating the list of documentation. ContentItem is called from [MainScreenUtil].
             */
            items(newsResult.getOrThrow()) { newsItem ->
                ContentItem(
                    title = newsItem.title ?: "",
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
