package no.usn.mob3000.ui.screens.info

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.SupabaseClientWrapper
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * @author 258030, Eirik
 * @created 2024-09-23
 */

@Composable
fun NewsScreen(
    onCreateNewsClick: () -> Unit,
    setNews: (List<News>) -> Unit,
    setSelectedNews: (News) -> Unit,
    onNewsClick: (News) -> Unit,
    filter: List<String>? = null
) {

    /* TODO: Extract data-handling code to data layer. */
    var existingNews by remember { mutableStateOf<List<News>>(emptyList()) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val boxSize = (screenWidth * 0.85f).coerceAtMost(300.dp)
    val height = (screenHeight * 0.25f).coerceAtMost(250.dp)

    LaunchedEffect(key1 = true) {
        try {
            val result = withContext(Dispatchers.IO) {
                val supabase = SupabaseClientWrapper.getClient()
                supabase.from("news").select().decodeList<News>()
            }
            existingNews = result
            setNews(result)
            Log.d("NewsScreen", "Fetched news: ${existingNews.size}")
        } catch (e: Exception) {
            Log.e("Function…", "Error fetching news", e)
        }
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DefaultButton,
                onClick = onCreateNewsClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create documentation")
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NewsBox(
                    title = stringResource(R.string.breaking_news),
                    description = stringResource(R.string.niemann_intro),
                    body = stringResource(R.string.niemann_report),
                    color = colorResource(id = R.color.beige_1),
                    size = boxSize,
                    height = height,
                    onClick = { /* TODO: Implement single-news-page */ }
                )
                NewsBox(
                    title = stringResource(R.string.shocking_news),
                    description = stringResource(R.string.magnus_intro),
                    body = stringResource(R.string.magnus_report),
                    color = colorResource(id = R.color.beige_1),
                    size = boxSize,
                    height = height,
                    onClick = { /* TODO: Implement single-news-page */ }
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                        .padding(innerPadding)
                ) {

                    items(existingNews) { news ->
                        CardButton(
                            text = news.title,
                            onClick = {
                                setSelectedNews(news)
                                onNewsClick(news)
                            }
                        )
                    }
                }

            }
        }
    }
}

@Serializable
data class News(
    val id: String,
    val created_at: String? = null,
    val modified_at: String? = null,
    val created_by: String? = null,
    val title: String,
    val summary: String,
    val content: String,
    val is_published: Boolean
)

@Composable
fun CardButton(
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF976646))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
    }
}



/**
 * @author 258030, Eirik
 * @created 2024-09-23
 */


@Composable
fun NewsBox(
    title: String,
    description: String,
    body: String,
    color: Color,
    height: androidx.compose.ui.unit.Dp,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .height(height)
            .size(size)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)) // Adds a shadow effect
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) // Adds a 1px border
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .padding(16.dp) // Adds padding inside the box
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            maxLines = 7,
            overflow = TextOverflow.Ellipsis // Truncates the text if it overflows
        )
    }
}

