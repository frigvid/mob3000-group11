package no.usn.mob3000.domain.model.game.board

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.game.GameContext
import com.github.bhlangonijr.chesslib.game.GameMode
import com.github.bhlangonijr.chesslib.game.VariationType

/**
 * The chess board's state.
 *
 * @author frigvid
 * @created 2024-11-03
 */
data class ChessBoardState(
    val board: Board =
        Board(
            GameContext(GameMode.HUMAN_VS_HUMAN, VariationType.NORMAL),
            true
        ),
    val draggedPiece: DraggedPiece? = null,
    val legalMoves: Set<Square> = emptySet(),
    val pendingPromotion: PendingPromotion? = null
)
