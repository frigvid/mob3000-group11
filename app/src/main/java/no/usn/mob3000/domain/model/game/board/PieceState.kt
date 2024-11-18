package no.usn.mob3000.domain.model.game.board

import androidx.compose.ui.geometry.Offset
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.PieceType
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square

/**
 * The chess board's piece's pending promotion state.
 *
 * @author frigvid
 * @created 2024-11-03
 */
data class PendingPromotion(
    val pieceType: PieceType,
    val pieceSide: Side,
    val squareRank: Rank,
    val dialogPosition: Pair<Float, Float>
)

/**
 * The chess board's piece's promotion information.
 *
 * @author frigvid
 * @created 2024-11-16
 */
data class PromotionState(
    val square: Square,
    val offset: Offset,
    val side: Side
)

/**
 * The chess board's piece drag-state.
 *
 * @author frigvid
 * @created 2024-11-03
 */
data class DraggedPiece(
    val piece: Piece,
    val fromSquare: Square,
    val currentX: Float,
    val currentY: Float
)
