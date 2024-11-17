package no.usn.mob3000.domain.model.game.board

/**
 * The chess board's game state.
 *
 * @author frigvid
 * @created 2024-11-17
 */
data class ChessBoardGameState(
    val status: String,
    val wins: Int,
    val losses: Int,
    val draws: Int
)
