package no.usn.mob3000.ui.screens.chess.train.group

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import no.usn.mob3000.Viewport

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
 * @author frigvid
 * @created 2024-09-24
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    onCreateGroupClick: () -> Unit,
    onReturnToOpeningClick: () -> Unit
) {
    Viewport (
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateGroupClick) {
                Icon(Icons.Default.Add, contentDescription = "Create Groups")
            }
        },
        topBarActions = {
            IconButton(onClick = onReturnToOpeningClick) {
                Icon(Icons.Default.Close, contentDescription = "Return to Openings")
            }
        }
    ) { innerPadding ->
        Box (
            Modifier.padding(innerPadding)
        ) {

        }
    }
}
