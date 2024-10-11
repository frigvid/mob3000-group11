package no.usn.mob3000.ui.screens.info

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
 * Screen where the users can read news and the admin can create news. Follows the same structure as
 * {@link no.usn.mob3000.ui.screens.chess.train.opening.OpeningScreen}
 *
 * TODO: Limit how "sensetive" {@code onNewsClick} is (limit the area which is clickable)
 *
 * @param onCreateNewsClick Callback function to navigate to the [CreateNewsScreen].
 * @param setNews ViewModel function to set the news.
 * @param setSelectedNews ViewModel function to set the selected news.
 * @param onNewsClick Callback function to navigate to the [ReadNewsScreen].
 *
 *
 * @author 258030, Eirik
 * @created 2024-09-23
 */

@Composable
fun NewsScreen(
    onCreateNewsClick: () -> Unit,
    setNews: (List<News>) -> Unit,
    setSelectedNews: (News) -> Unit,
    onNewsClick: (News) -> Unit
) {

    var existingNews by remember { mutableStateOf<List<News>>(emptyList()) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val height = (screenHeight * 0.25f).coerceAtMost(250.dp)
    val width = (screenWidth * 0.85f).coerceAtMost(300.dp)

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
                Icon(Icons.Default.Add, contentDescription = "Create news")
            }
        }) { innerPadding ->

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    items(existingNews) { news ->
                        CardButton(
                            color = colorResource(id = R.color.beige_1),
                            height = height,
                            title = news.title,
                            summary = news.summary,
                            content = news.content,
                            onClick = {
                                setSelectedNews(news)
                                onNewsClick(news)
                            }
                        )
                    }
                }

            }
        }

/**
 * Data class for the news table in the database. Not all values are used currently, but all of them are made available
 * for future use. This class would ideally be abstracted or placed within its own relevant package with other db-related
 * classes/functions.
 */
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
    title: String,
    summary: String,
    content: String,
    height: androidx.compose.ui.unit.Dp,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .border(
                BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .height(height)
                .background(color = color, shape = RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = summary,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = content,
                fontSize = 10.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


