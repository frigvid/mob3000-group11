package no.usn.mob3000.domain.helper.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.put
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
    if (jsonPgnArray.isNullOrEmpty()) return ""

    // NOTE: See Logger.kt TODO.
    // TODO: Fix JUnit mockup. Logger.d("Processing ${jsonMoves.size} moves: $jsonMoves")

    return try {
        val moves = convertMovesToPgnFormat(jsonPgnArray)
        formatMovesWithNumbers(moves)
    } catch (error: Exception) {
        //TODO: Fix JUnit mockup. Logger.e("Failed to convert moves to PGN $error")
        throw IllegalArgumentException("Failed to convert moves to PGN: ${error.message}", error)
    }
}

/**
 * Converts standard PGN notation back to a JSON PGN move set fragment array.
 *
 * This also performs some safety checks using regular expressions to
 * validate the structure of the inputted PGN notation.
 *
 * @param pgn Standard PGN string.
 * @return [JsonArray] containing move fragments.
 * @throws IllegalArgumentException If PGN notation isn't valid.
 * @author frigvid
 * @created 2024-11-14
 */
fun convertPgnToJsonPgnArray(
    pgn: String?
): JsonArray {
    /* Practically the same check, but it doesn't hurt to be paranoid. */
    if (pgn == null) return buildJsonArray { }
    if (pgn.isBlank()) return buildJsonArray { }

    val moveGroups = pgn.split("""\s+(?=\d+\.)""".toRegex())
    if (
        !moveGroups.all {
            it.matches("""^\d+\.\s+[NBRQK]?[a-h][1-8]-[a-h][1-8](?:\s+[NBRQK]?[a-h][1-8]-[a-h][1-8])?$""".toRegex())
        }
    ) {
        throw IllegalArgumentException("Invalid PGN format: Moves must follow pattern: <number>. <move> [<move>]")
    }

    val moves = mutableListOf<JsonObject>()
    val movePattern = """([NBRQK])?([a-h][1-8])-([a-h][1-8])""".toRegex()

    moveGroups.forEach { group ->
        val fragments = group.substringAfter(". ").split(" ")

        fragments.forEach { move ->
            val result = movePattern.matchEntire(move)
                ?: throw IllegalArgumentException("Invalid move format: $move")

            val (piece, from, to) = result.destructured

            val pieceNotation = when (piece) {
                "N" -> "n"
                "B" -> "b"
                "R" -> "r"
                "Q" -> "q"
                "K" -> "k"
                "" -> "p"

                else -> throw IllegalArgumentException("Invalid piece notation: $piece")
            }

            moves.add(buildJsonObject {
                put("to", to)
                put("from", from)
                put("piece", pieceNotation)
            })
        }
    }

    return JsonArray(moves)
}

/**
 * Converts individual moves to proper PGN format.
 *
 * @author frigvid
 * @created 2024-11-16
 */
private fun convertMovesToPgnFormat(jsonMoves: JsonArray): List<String> {
    return jsonMoves.map { element ->
        try {
            val move = Json.decodeFromJsonElement<ChessMove>(element)
            formatSingleMove(move)
        } catch (error: Exception) {
            throw IllegalArgumentException("Invalid move format", error)
        }
    }
}

/**
 * Formats a single move into proper PGN notation.
 *
 * @author frigvid
 * @created 2024-11-16
 */
private fun formatSingleMove(move: ChessMove): String {
    return when (move.piece.lowercase()) {
        "p" -> {
            if (move.from[0] != move.to[0]) {
                "${move.from[0]}x${move.to}"
            } else {
                move.to
            }
        }

        "n" -> "N${move.from}${move.to}"
        "b" -> "B${move.from}${move.to}"
        "r" -> "R${move.from}${move.to}"
        "q" -> "Q${move.from}${move.to}"
        "k" -> "K${move.from}${move.to}"

        else -> throw IllegalArgumentException("Invalid piece type: ${move.piece}")
    }
}

/**
 * Formats the moves list with move numbers.
 *
 * @author frigvid
 * @created 2024-11-16
 */
private fun formatMovesWithNumbers(moves: List<String>): String {
    return moves.chunked(2).mapIndexed { index, pair ->
        val moveNumber = index + 1
        when (pair.size) {
            2 -> "$moveNumber. ${pair[0]} ${pair[1]}"
            1 -> "$moveNumber. ${pair[0]}"
            else -> ""
        }
    }.joinToString(" ").trim()
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

/**
 * Represents a chess move with piece type and square coordinates.
 *
 * @author frigvid
 * @created 2024-11-16
 */
@Serializable
private data class ChessMove(
    @SerialName("piece")
    val piece: String,
    @SerialName("from")
    val from: String,
    @SerialName("to")
    val to: String
)
