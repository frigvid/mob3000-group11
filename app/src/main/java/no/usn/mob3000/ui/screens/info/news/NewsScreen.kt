package no.usn.mob3000.ui.screens.info.news

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.ui.theme.DefaultListItemBackground

/**
 * Screen for displaying a list of news articles.
 */
@Composable
fun NewsScreen(
    news: List<NewsDto>,
    onNewsClick: (NewsDto) -> Unit,
    onCreateNewsClick: () -> Unit,
    setNewsList: (List<NewsDto>) -> Unit,
    setSelectedNews: (NewsDto) -> Unit,
    clearSelectedNews: () -> Unit
) {
    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        clearSelectedNews()
        val dbUtilities = DbUtilities()

        try {
            val result = dbUtilities.fetchItems("news", NewsDto.serializer())
            result.onSuccess { items ->
                setNewsList(items)
            }
        } catch (e: Exception) {
            Log.e("News", "Error fetching news articles", e)
        }
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(news) { newsItem ->
                NewsItem(
                    news = newsItem,
                    onClick = {
                        setSelectedNews(newsItem)
                        onNewsClick(newsItem)
                    }
                )
            }
        }
    }
}

/**
 * Composable function to display individual news items.
 */
@Composable
fun NewsItem(
    news: NewsDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground),
        border =
        if (news.isPublished) null
        else BorderStroke(width = 2.dp, color = Color(0xFFFF0000))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = news.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            news.summary?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
