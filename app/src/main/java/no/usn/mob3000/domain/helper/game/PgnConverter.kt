package no.usn.mob3000.domain.helper.game

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import no.usn.mob3000.data.model.game.OpeningsDto

/**
 * Converts a JSON PGN array containing PGN move set fragments into a standard PGN string.
 *
 * ## JSON PGN array structure
 *
 * ```json
 * [
 *   {
 *     "to": "f3",
 *     "from": "f2",
 *     "piece": "p"
 *   },
 *   {
 *     "to": "e5",
 *     "from": "e7",
 *     "piece": "p"
 *   },
 *   {
 *     "to": "g4",
 *     "from": "g2",
 *     "piece": "p"
 *   },
 *   {
 *     "to": "h4",
 *     "from": "d8",
 *     "piece": "q"
 *   }
 * ]
 * ```
 *
 * ## Piece notation
 *
 * - p = pawn.
 * - n = knight.
 * - b = bishop.
 * - r = rook.
 *    - NOTE: In Standard Algebraic Notation (SAN), rooks do not have a letter.
 * - q = queen.
 * - k = king.
 *
 * @param jsonPgnArray The JSON PGN array containing the PGN move set fragments.
 * @return A standard PGN string.
 * @throws IllegalArgumentException If the JSON structure is invalid.
 * @author frigvid
 * @created 2024-11-14
 */
fun convertJsonPgnArrayToPgn(
    jsonPgnArray: JsonArray?
): String {
    if (jsonPgnArray == null) return ""

    val moves = mutableListOf<String>()

    for (step in jsonPgnArray) {
        try {
            val moveObj = step.jsonObject
            val piece = moveObj["piece"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Missing piece")
            val from = moveObj["from"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Missing from")
            val to = moveObj["to"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Missing to")

            val standardAlgebraicNotation = when (piece.lowercase()) {
                "p" -> ""
                "n" -> "N"
                "b" -> "B"
                "r" -> "R"
                "q" -> "Q"
                "k" -> "K"

                else -> throw IllegalArgumentException("Invalid piece type: $piece")
            }

            val move = "$standardAlgebraicNotation$from-$to"

            moves.add(move)
        } catch (error: Exception) {
            throw IllegalArgumentException("Invalid move format in PGN array", error)
        }
    }

    /* Prefix move sets with move numbers. */
    return moves.chunked(2).mapIndexed { index, movePair ->
        "${index + 1}. ${movePair[0]}${if (movePair.size > 1) { " ${movePair[1]}" } else { "" } }"
    }.joinToString(" ")
}

/**
 * Extension function for OpeningsDto to convert its JSON PGN array to a
 * standard PGN string.
 *
 * @receiver [OpeningsDto] containing the JSON PGN array.
 * @return Standard PGN string
 * @author frigvid
 * @created 2024-11-14
 */
fun OpeningsDto.toPgn(): String {
    return convertJsonPgnArrayToPgn(this.pgn)
}
