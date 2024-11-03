package no.usn.mob3000.ui.screens.chess

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.screens.chess.game.ChessBoard
import no.usn.mob3000.ui.theme.DefaultButton
import no.usn.mob3000.ui.theme.DefaultListItemBackground

/**
 * Screen for users to play chess.
 *
 * This consists of:
 * - A chess board (currently using a placeholder image)
 * - The current game status
 * - Game statistics (wins, losses, draws)
 * - Control buttons for resetting the board, undoing moves, and switching to multiplayer
 *
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun PlayScreen() {
    /* TODO: Extract to ViewModel as necessary. */
    val gameStatus by remember { mutableStateOf("Ongoing") }
    val wins by remember { mutableIntStateOf(5) }
    val losses by remember { mutableIntStateOf(3) }
    val draws by remember { mutableIntStateOf(2) }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.play_game_status),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = gameStatus,
                        fontSize = 16.sp
                    )
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.play_statistics),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatisticItem(stringResource(R.string.play_wins), wins)
                        StatisticItem(stringResource(R.string.play_losses), losses)
                        StatisticItem(stringResource(R.string.play_draws), draws)
                    }
                }
            }

            ChessBoard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                startingPosition = "3qk3/7P/8/8/8/8/7p/3QK3 w - - 0 1"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: Implement reset board logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = DefaultButton),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) { Text(stringResource(R.string.play_reset_board)) }

                Button(
                    onClick = { /* TODO: Implement undo move logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = DefaultButton),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) { Text(stringResource(R.string.play_undo_move)) }

                Button(
                    onClick = { /* TODO: Implement switch to multiplayer logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = DefaultButton),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) { Text(stringResource(R.string.play_multiplayer)) }
            }
        }
    }
}

/**
 * Function that displays a single statistic item.
 *
 * @param label The text label for the statistic (e.g., "Wins", "Losses", "Draws").
 * @param value The numeric value of the statistic.
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun StatisticItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp)
        Text(text = value.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
