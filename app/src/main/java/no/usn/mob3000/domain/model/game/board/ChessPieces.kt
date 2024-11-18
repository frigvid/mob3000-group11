package no.usn.mob3000.domain.model.game.board

import com.github.bhlangonijr.chesslib.Piece
import no.usn.mob3000.R

/**
 * Map of chess pieces to resource drawable pieces.
 *
 * @author frigvid
 * @created 2024-11-03
 */
object ChessPieces {
    val pieceToResourceMap = mapOf(
        Piece.BLACK_PAWN to R.drawable.chess_pawn_black,
        Piece.BLACK_ROOK to R.drawable.chess_rook_black,
        Piece.BLACK_KNIGHT to R.drawable.chess_knight_black,
        Piece.BLACK_BISHOP to R.drawable.chess_bishop_black,
        Piece.BLACK_QUEEN to R.drawable.chess_queen_black,
        Piece.BLACK_KING to R.drawable.chess_king_black,
        Piece.WHITE_PAWN to R.drawable.chess_pawn_white,
        Piece.WHITE_ROOK to R.drawable.chess_rook_white,
        Piece.WHITE_KNIGHT to R.drawable.chess_knight_white,
        Piece.WHITE_BISHOP to R.drawable.chess_bishop_white,
        Piece.WHITE_QUEEN to R.drawable.chess_queen_white,
        Piece.WHITE_KING to R.drawable.chess_king_white
    )
}
