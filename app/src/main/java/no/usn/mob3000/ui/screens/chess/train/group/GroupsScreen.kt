package no.usn.mob3000.ui.screens.chess.train.group

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.SupabaseClientWrapper
import no.usn.mob3000.ui.theme.DefaultButton
import no.usn.mob3000.ui.screens.chess.train.opening.OpeningsScreen
import no.usn.mob3000.ui.theme.DefaultListItemBackground

/**
 * This shows the various chess opening groups that have been created by the active user.
 *
 * Its sibling screen, which is accessible from this one, is the OpeningsScreen.
 *
 * TODO: Inform the user that this is only available to logged in users.
 *       Alternatively, make it so that the app caches this locally, and *if*
 *       a user is created, then it takes the cached data and syncs it to the server.
 *       Falling back to local-only if the user logs out again.
 *
 * NOTE: To actually test this, since login hasn't been implemented, remember to temporarily
 *       disable RLS on the repertoire table. It'll simply display nothing if it's on since it
 *       depends on the currently logged in user's ID matching any "usr" in for any given row.
 *
 * @param onCreateGroupClick Callback function to navigate to [CreateGroupScreen].
 * @param onReturnToOpeningClick Callback function to navigate to [OpeningsScreen].
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun GroupsScreen(
    onCreateGroupClick: () -> Unit,
    onReturnToOpeningClick: () -> Unit
) {
    /* TODO: Extract data-handling code to data layer. */
    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        try {
            val result = withContext(Dispatchers.IO) {
                val supabase = SupabaseClientWrapper.getClient()
                supabase.from("repertoire").select().decodeList<Group>()
            }

            groups = result

            Log.d("GroupsScreen", "Fetched groups: ${groups.size}")
        } catch (e: Exception) {
            Log.e("GroupsScreen", "Error fetching groups", e)
        }
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DefaultButton,
                onClick = onCreateGroupClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Group")
            }
        },
        topBarActions = {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(DefaultButton),
                onClick = onReturnToOpeningClick
            ) {
                Icon(Icons.Default.Close, contentDescription = "Return to Openings")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            items(groups) { group ->
                GroupItem(group)
            }
        }
    }
}

/* Todo: Extract to data layer. */
@Serializable
data class Group(
    val id: String,
    val timestamp: String,
    val usr: String?,
    val title: String,
    val description: String?,
    val openings: List<String>
)

/**
 * Creates a list item for the scrollable list from any given group given.
 *
 * @param group The group data object from the database.
 * @see [Group]
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun GroupItem(group: Group) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = group.description ?: "No description available",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Openings:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                /* TODO: Long-term. Sub-list of expandable openings. Or clickables that take the
                 *       user to the given opening. Shouldn't need any safety-checks for if the
                 *       opening is available or not. With RLS on, the user should only be able to
                 *       access their own, or the ones the developers provide.
                 */
                group.openings.forEach { opening ->
                    Text(
                        text = "• $opening",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
