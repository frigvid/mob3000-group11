package no.usn.mob3000.ui.screens.chess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.helper.game.convertPgnToFen
import no.usn.mob3000.domain.model.game.board.ChessBoardGameState
import no.usn.mob3000.domain.model.game.board.ChessBoardState
import no.usn.mob3000.domain.model.game.board.PracticeMode
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.game.board.ChessBoard

/**
 * Screen for users to play chess.
 *
 * This consists of:
 * - A chess board (currently using a placeholder image)
 * - The current game status
 * - Game statistics (wins, losses, draws)
 * - Control buttons for resetting the board, undoing moves, and switching to multiplayer
 *
 * @param openingsList The list of openings to train against. May only contain one, or multiple.
 * @param boardState The chess board's state. The logic layer, if you will.
 * @param gameState The chess board's game state; the status and user stats.
 * @param onResetBoardClick Callback function to reset the board.
 * @param onUndoMoveClick Callback function to undo a move.
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun PlayScreen(
    openingsList: List<Opening>?,
    boardState: StateFlow<ChessBoardState>,
    gameState: StateFlow<ChessBoardGameState>,
    onResetBoardClick: () -> Unit,
    onUndoMoveClick: () -> Unit
) {
    val statefulBoard by boardState.collectAsState()
    val statefulGame by gameState.collectAsState()

    Logger.d("PlayScreen: Collected board state: $statefulBoard")
    Logger.d("PlayScreen: Collected game state: $statefulGame")

    /* If list is more than one long, assume that practice mode is group training. */
    val practiceMode = if (openingsList != null && openingsList.size == 1) {
        Logger.d("Practice mode: ${PracticeMode.SINGLE.name}")
        PracticeMode.SINGLE
    } else if (openingsList != null && openingsList.size > 1) {
        Logger.d("Practice mode: ${PracticeMode.GROUP.name}")
        PracticeMode.GROUP
    } else {
        Logger.d("Practice mode: ${PracticeMode.NONE.name}")
        PracticeMode.NONE
    }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card( colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
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

                    Logger.d("PlayScreen: About to display status: ${statefulGame.status}")
                    Text(
                        text = statefulGame.status,
                        fontSize = 16.sp
                    )
                }
            }

            Card( colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
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
                        StatisticItem(stringResource(R.string.play_wins), statefulGame.wins)
                        StatisticItem(stringResource(R.string.play_losses), statefulGame.losses)
                        StatisticItem(stringResource(R.string.play_draws), statefulGame.draws)
                    }
                }
            }

            if (practiceMode.name == PracticeMode.SINGLE.name) {
                ChessBoard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    startingPosition = convertPgnToFen(openingsList?.first()?.moves ?: "")
                )
            } else if (practiceMode == PracticeMode.GROUP) {
                TODO("Group practice is not implemented yet.")
            } else {
                ChessBoard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    // TODO@frigvid: Remove after finishing promotion testing.
                    //               startingPosition = "3qk3/7P/8/8/8/8/7p/3QK3 w - - 0 1"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onResetBoardClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.play_reset_board))
                }

                Button(
                    onClick = onUndoMoveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text(stringResource(R.string.play_undo_move))
                }

                /* TODO: Currently, this button is disabled because we're technically always in
                 *       local multiplayer mode. Either some form of custom algorithm, a simple
                 *       randomizer, or Stockfish would be needed before this is ready to be enabled
                 *       again.
                 */
                Button(
                    onClick = { /* TODO: Implement switch to multiplayer logic */ },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.play_multiplayer))
                }
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
private fun StatisticItem(
    label: String,
    value: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp)
        Text(text = value.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
