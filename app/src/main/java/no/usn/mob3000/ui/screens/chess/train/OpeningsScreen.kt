package no.usn.mob3000.ui.screens.chess.train

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import no.usn.mob3000.Viewport

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
