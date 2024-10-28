package no.usn.mob3000.ui.screens.chess.train.opening

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This shows the various chess openings that are available by default, and that
 * are created by users.
 *
 * Its sibling screen, which is accessible from this one, is the GroupsScreen.
 *
 * @param onGroupsClick Callback function to navigate to the [GroupsScreen].
 * @param onCreateOpeningClick Callback function to navigate to the [CreateOpeningScreen].
 * @param onOpeningClick Callback function to navigate to the [OpeningDetailsScreen].
 * @param setOpenings ViewModel function to set the openings.
 * @param setSelectedOpening ViewModel function to set the selected opening.
 * @param filter TODO: Optional list of string IDs to filter openings
 * @author frigvid
 * @created 2024-09-24
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpeningsScreen(
    onGroupsClick: () -> Unit,
    onCreateOpeningClick: () -> Unit,
    onOpeningClick: (Opening) -> Unit,
    setOpenings: (List<Opening>) -> Unit,
    setSelectedOpening: (Opening) -> Unit,
    filter: List<String>? = null
) {
    /* TODO: Extract data-handling code to data layer. */
    var openings by remember { mutableStateOf<List<Opening>>(emptyList()) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(key1 = true) {
        try {
            val result = withContext(Dispatchers.IO) {
                val supabase = SupabaseClientWrapper.getClient()
                supabase.from("openings").select().decodeList<Opening>()
            }
            openings = result
            setOpenings(result)
            Log.d("OpeningsScreen", "Fetched openings: ${openings.size}")
        } catch (e: Exception) {
            Log.e("OpeningsScreen", "Error fetching openings", e)
        }
    }

    Viewport (
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DefaultButton,
                onClick = onCreateOpeningClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create opening")
            }
        },
        topBarActions = {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(DefaultButton),
                onClick = onGroupsClick
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Groups")
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
                               .padding(innerPadding)
        ) {
            items(openings) { opening ->
                CardButton(
                    text = opening.title,
                    imageResource = R.drawable.placeholder_chess,
                    onClick = {
                        /* TODO: Go to details page. Might need to implement a ViewModel for this. */
                        Log.d("OpeningsScreen", opening.pgn.toString())
                        setSelectedOpening(opening)
                        onOpeningClick(opening)
                    }
                )
            }
        }
    }
}

/* Todo: Extract to data layer. */
@Serializable
data class Opening(
    val id: String,
    val created_by: String? = null,
    val title: String,
    val description: String,
    val pgn: List<Map<String, String>>,
    val timestamp: String
)

/**
 * Displays a card, with a title and a thumbnail of the opening.
 *
 * TODO: Either generate the thumbnail, or otherwise display the finished steps akin to the website,
 *       instead of showing a placeholder.
 *
 * @param text The title to display above the picture.
 * @param imageResource The thumbnail to display. Temporary, should be generated and not a reference.
 * @param onClick What the card does when clicked.
 * @author frigvid
 * @created 2024-10-08
 */
@Composable
fun CardButton(
    text: String,
    imageResource: Int,
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
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
