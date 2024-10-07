package no.usn.mob3000.ui.screens.chess.train

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import no.usn.mob3000.Viewport
import no.usn.mob3000.data.SupabaseClientWrapper
import java.util.UUID

/**
 * This shows the various chess openings that are available by default, and that
 * are created by users.
 *
 * Its sibling screen, which is accessible from this one, is the GroupsScreen.
 *
 * @param onGroupsClick Callback function to navigate to the Groups screen
 * @param onCreateOpeningClick Callback function to navigate to the Create Opening screen
 * @param filter TODO: Optional list of string IDs to filter openings
 * @author frigvid
 * @created 2024-09-24
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpeningsScreen(
    onGroupsClick: () -> Unit,
    onCreateOpeningClick: () -> Unit,
    filter: List<String>? = null
) {
    /* Temporary example on how to fetch data from the back-end. */
    LaunchedEffect(key1 = true) {
        try {
            val result = withContext(Dispatchers.IO) {
                val supabase = SupabaseClientWrapper.getClient()
                supabase.from("public", "openings").select {
                    filter {
                        eq("id", "0976138f-46fe-45b5-aa7e-9272f4dbab87")
                    }
                }.decodeSingle<Opening>()
            }
            Log.d("OpeningsScreen", "Fetched opening data: $result")
        } catch (e: Exception) {
            Log.e("OpeningsScreen", "Error fetching opening", e)
        }
    }

    Viewport (
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateOpeningClick) {
                Icon(Icons.Default.Add, contentDescription = "Create opening")
            }
        },
        topBarActions = {
            IconButton(onClick = onGroupsClick) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Groups")
            }
        }
    ) { innerPadding ->
        Box (
            Modifier.padding(innerPadding)
        ) {

        }
    }
}

/* Temporary serializable data class. */
@Serializable
data class Opening(
    val id: String,
    val created_by: String? = null,
    val title: String,
    val description: String,
    val pgn: List<Map<String, String>>,
    val timestamp: String
)
