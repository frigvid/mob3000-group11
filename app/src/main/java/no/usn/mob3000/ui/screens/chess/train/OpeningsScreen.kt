package no.usn.mob3000.ui.screens.chess.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 * This shows the various chess openings that are available by default, and that
 * are created by users.
 *
 * Its sibling screen, which is accessible from this one, is the GroupsScreen.
 *
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