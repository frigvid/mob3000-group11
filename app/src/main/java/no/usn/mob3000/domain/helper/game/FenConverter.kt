package no.usn.mob3000.domain.helper.game

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.MoveList
import no.usn.mob3000.domain.helper.Logger

/**
 * Converts a PGN notation string into FEN notation.
 *
 * Uses a chess board to track position changes while applying PGN moves,
 * then returns the final FEN position.
 *
 * @param pgn The PGN notation string to convert.
 * @return FEN string representing the final position, or starting position if PGN is invalid/empty.
 * @throws IllegalArgumentException if the PGN string cannot be parsed.
 * @author frigvid
 * @created 2024-11-16
 */
fun convertPgnToFen(pgn: String): String {
    if (pgn.isBlank()) return Board().fen

    try {
        val cleanPgn = pgn.replace(Regex("""\s*(1-0|0-1|1/2-1/2|\*)\s*$"""), "")

        val moveList = MoveList()
        moveList.loadFromSan(cleanPgn)

        val board = Board()
        for (move in moveList) {
            if (!board.doMove(move)) {
                throw IllegalArgumentException("Invalid move: $move")
            }
        }

        Logger.d("Input: ${board.fen}")
        Logger.d("Output: ${convertFenToPgn(board.fen)}")

        return board.fen
    } catch (error: Exception) {
        throw IllegalArgumentException("Invalid PGN notation: ${error.message}", error)
    }
}

/**
 * Converts a FEN notation string into PGN notation.
 *
 * @param fen The FEN notation string to convert.
 * @return PGN string representing the position.
 * @throws IllegalArgumentException if the FEN string is invalid.
 * @author frigvid
 * @created 2024-11-16
 */
fun convertFenToPgn(fen: String): String {
    if (fen.isBlank()) return "No FEN notation inputted."

    try {
        return MoveList(fen).toSanWithMoveNumbers()
    } catch (error: Exception) {
        throw IllegalArgumentException("Invalid FEN notation: ${error.message}", error)
    }
}
