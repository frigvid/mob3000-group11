package no.usn.mob3000.ui.screens.chess.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

/**
 * This shows the various chess openings that are available by default, and that
 * are created by users.
 *
 * Its sibling screen, which is accessible from this one, is the GroupsScreen.
 *
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun OpeningsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.openings_title))
    }
}