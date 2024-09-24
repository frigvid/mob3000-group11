package no.usn.mob3000.ui.screens.chess.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

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
@Composable
fun GroupsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.groups_title))
    }
}