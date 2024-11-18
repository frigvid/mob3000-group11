package no.usn.mob3000.domain.model.game.board

import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square

/**
 * Chess board events.
 *
 * @author frigvid
 * @created 2024-11-03
 */
sealed class ChessBoardEvent {
    data class InitializeBoard(val fen: String) : ChessBoardEvent()
    data class OnPieceDragStart(val square: Square, val x: Float, val y: Float) : ChessBoardEvent()
    data class OnPieceDragged(val x: Float, val y: Float) : ChessBoardEvent()
    data class OnPieceDragEnd(val toSquare: Square?) : ChessBoardEvent()
    data class OnPromotionPieceSelected(val piece: Piece) : ChessBoardEvent()
}
