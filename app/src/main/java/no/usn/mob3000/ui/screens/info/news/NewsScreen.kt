package no.usn.mob3000.ui.screens.info.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.usn.mob3000.ui.theme.DefaultListItemBackground
import no.usn.mob3000.ui.screens.info.docs.DocumentationScreen
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for displaying a list of news articles.
 *
 * This screen shows a scrollable list of news articles. Each article is represented
 * by a card showing its title, summary, and creation date. Users can click on an
 * article to view its details. There's also a floating action button to create new articles.
 *
 * TODO: This and [DocumentationScreen] can probably be made more generic, so they depend
 *       on fellow components similar to the web-version.
 * TODO: Adding the ability to set an image for news would make them easier to differentiate
 *       from the [DocumentationScreen].
 *
 * @param news The list of news articles stored in the ViewModel's state.
 * @param onNewsClick Callback function to navigate to the [News] object's details screen.
 * @param onCreateNewsClick Callback function to navigate to the create news screen.
 * @param setNewsList ViewModel function to store the list of [News] objects in state.
 * @param setSelectedNews ViewModel function to store a specific [News] object in state.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-09-23
 */
@Composable
fun NewsScreen(
    news: List<News>,
    onNewsClick: (News) -> Unit,
    onCreateNewsClick: () -> Unit,
    setNewsList: (List<News>) -> Unit,
    setSelectedNews: (News) -> Unit
) {
    /* TODO: Replace dummy data with fetched data from data layer and cache it in ViewModel state.
     *       Maybe check for news in the background, so there's less of a load time?
     */
    LaunchedEffect(Unit) {
        val dummyNews = listOf(
            News(
                "1",
                Date(),
                Date(),
                "User1",
                "New Chess Tournament Announced",
                "Exciting chess tournament coming up next month",
                "A major chess tournament has been announced for next month. Players from around the world are expected to participate...",
                true
            ),
            News(
                "2",
                Date(),
                Date(),
                "User2",
                "Chess AI Beats World Champion",
                "Artificial Intelligence makes history in chess",
                "In a shocking turn of events, a new chess AI has defeated the current world champion in a series of matches...",
                true
            ),
            News(
                "3",
                Date(),
                Date(),
                "User3",
                "Chess Strategy Workshop",
                "Learn from the masters",
                "A week-long chess strategy workshop is being organized, featuring lectures from grandmasters...",
                false
            )
        )

        setNewsList(dummyNews)
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewsClick,
                containerColor = DefaultButton
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Create News"
                )
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
 *
 * @param news The [News] object to display.
 * @param onClick Callback function to navigate to the [News] object's details screen.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun NewsItem(
    news: News,
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
                text = news.title,
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

            Text(
                text = stringResource(R.string.news_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.news_date_pattern), Locale.getDefault()).format(news.createdAt)}",
                fontSize = 12.sp
            )
        }
    }
}

/**
 * TODO: Extract to data layer and fix for use with fetched data.
 *
 * @property id Unique identifier for the news article.
 * @property createdAt Date and time when the article was created.
 * @property modifiedAt Date and time when the article was last modified.
 * @property createdBy Identifier of the user who created the article.
 * @property title Title of the news article.
 * @property summary Brief summary of the article content.
 * @property content Full content of the news article.
 * @property isPublished Boolean indicating whether the article is published or in draft state
 * @author frigvid
 * @created 2024-10-12
 */
data class News(
    val id: String,
    val createdAt: Date,
    val modifiedAt: Date,
    val createdBy: String,
    val title: String,
    val summary: String?,
    val content: String?,
    val isPublished: Boolean
)
