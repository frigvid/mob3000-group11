package no.usn.mob3000.ui.screens.chess.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
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
