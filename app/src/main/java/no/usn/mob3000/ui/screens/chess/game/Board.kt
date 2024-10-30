package no.usn.mob3000.ui.screens.chess.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.theme.ChessboardCellDark
import no.usn.mob3000.ui.theme.ChessboardCellLight

/**
 * A customizable chessboard component that displays a standard 8x8 chess board
 * with pieces in their initial positions.
 *
 * TODO: Make this better.
 * TODO: Add cell identifier text.
 * TODO: Make pieces movable.
 *
 * @param modifier Optional modifier for customizing the chessboard's appearance
 * @author frigvid
 * @created 2024-10-30
 */
@Composable
fun Chessboard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (row in 7 downTo 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (col in 0..7) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    if ((row + col) % 2 == 0) ChessboardCellLight
                                    else ChessboardCellDark
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            getPieceForPosition(row, col)?.let { resourceId ->
                                Image(
                                    painter = painterResource(resourceId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxSize(0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Determines which chess piece (if any) should be displayed at a given position.
 *
 * @param row The row number (0-7)
 * @param col The column number (0-7)
 * @return The resource ID for the chess piece icon, or null if the square is empty
 * @author frigvid
 * @created 2024-10-30
 */
private fun getPieceForPosition(row: Int, col: Int): Int? {
    return when (row) {
        1 -> R.drawable.chess_pawn_white
        0 -> when (col) {
            0, 7 -> R.drawable.chess_rook_white
            1, 6 -> R.drawable.chess_knight_white
            2, 5 -> R.drawable.chess_bishop_white
            3 -> R.drawable.chess_queen_white
            4 -> R.drawable.chess_king_white
            else -> null
        }

        6 -> R.drawable.chess_pawn_black
        7 -> when (col) {
            0, 7 -> R.drawable.chess_rook_black
            1, 6 -> R.drawable.chess_knight_black
            2, 5 -> R.drawable.chess_bishop_black
            3 -> R.drawable.chess_queen_black
            4 -> R.drawable.chess_king_black
            else -> null
        }

        else -> null
    }
}
