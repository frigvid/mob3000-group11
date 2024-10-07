package no.usn.mob3000.ui.screens.chess

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 * This is the history screen, and should display the user's previous games.
 *
 * From here, they should be able to expand an entry, and be able to step
 * back and forwards through their game.
 *
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun HistoryScreen() {
    Viewport { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                               .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.play_title))
        }
    }
}