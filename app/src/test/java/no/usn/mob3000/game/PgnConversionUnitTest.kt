package no.usn.mob3000.game

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import no.usn.mob3000.domain.helper.game.convertJsonPgnArrayToPgn
import no.usn.mob3000.domain.helper.game.convertPgnToJsonPgnArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val TAG: String = "> TEST :app:PgnConversionUnitTest"

/**
 * PGN notation converter tests.
 *
 * This includes both one-off and bi-directional tests for
 * PGN notation, JSON PGN array, and both.
 *
 * Docstrings for individual tests aren't included, as the
 * test's names are made to be at least mostly self-explanatory.
 *
 * @author frigvid
 * @created 2024-11-14
 */
class PgnConversionUnitTest {
    private val validJsonPgnArray = """[
        {
            "to": "f3",
            "from": "f2",
            "piece": "p"
        },
        {
            "to": "e5",
            "from": "e7",
            "piece": "p"
        },
        {
            "to": "g4",
            "from": "g2",
            "piece": "p"
        },
        {
            "to": "h4",
            "from": "d8",
            "piece": "q"
        }
    ]""".trimMargin()

    private val malformedJsonPgnArray = """[
        {
            "to": "f3",
            "piece": "p"
        },
        {
            "from": "e7",
            "piece": "x"
        }
    ]""".trimMargin()

    /* JSON PGN array to PGN tests. */
    @Test
    fun `convertJsonPgnArrayToPgn converts valid JSON PGN array`() {
        println("$TAG :: Valid JSON PGN array parsing")

        val jsonArray = Json.decodeFromString<JsonArray>(validJsonPgnArray)
        val result = convertJsonPgnArrayToPgn(jsonArray)
        println("> Result: $result")

        assertEquals("1. f3 e5 2. g4 Qd8h4", result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertJsonPgnArrayToPgn converts malformed JSON PGN array`() {
        println("$TAG :: Malformed JSON PGN array parsing")

        val jsonArray = Json.decodeFromString<JsonArray>(malformedJsonPgnArray)
        convertJsonPgnArrayToPgn(jsonArray)
    }

    @Test
    fun `convertJsonPgnArrayToPgn handles null input`() {
        println("$TAG :: Null input parsing")

        val result = convertJsonPgnArrayToPgn(null)
        assertTrue(result.isEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertJsonPgnArrayToPgn throws exception for invalid piece type`() {
        println("$TAG :: Malformed JSON PGN array parsing")

        val jsonArray = Json.decodeFromString<JsonArray>("""[{"to": "f3", "from": "f2", "piece": "x"}]""")
        convertJsonPgnArrayToPgn(jsonArray)
    }

    /* PGN to JSON PGN array tests. */
    @Test(expected = IllegalArgumentException::class)
    fun `convertPgnToJsonPgnArray converts standard PGN notation correctly`() {
        println("$TAG :: Standard PGN to JSON array conversion")

        val pgnString = "1. f3 e5 2. g4 Qd8h4"
        val result = convertPgnToJsonPgnArray(pgnString)
        println("Result: $result")

        val expected = Json.decodeFromString<JsonArray>(validJsonPgnArray)
        assertEquals(expected, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `bidirectional conversion preserves move information`() {
        println("$TAG :: Bidirectional conversion test")

        val originalPgn = "1. f3 e5 2. g4 Qd8h4"
        val jsonArray = convertPgnToJsonPgnArray(originalPgn)
        println("JSON PGN array: $jsonArray")
        val convertedBackPgn = convertJsonPgnArrayToPgn(jsonArray)
        println("SAN PGN notation: $convertedBackPgn")

        assertEquals(originalPgn, convertedBackPgn)
    }

    @Test
    fun `convertPgnToJsonPgnArray handles empty input`() {
        println("$TAG :: Empty PGN string conversion")

        val result = convertPgnToJsonPgnArray("")
        println("Result: $result")

        assertTrue(result.isEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertPgnToJsonPgnArray throws exception for invalid PGN format`() {
        println("$TAG :: Malformed PGN format handling")

        convertPgnToJsonPgnArray("1. f2-f3 e7-e52-g4 Qd8-h4")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertPgnToJsonPgnArray detects invalid move format`() {
        println("$TAG :: Invalid move format detection")

        val invalidPgn = "1. f2-f3 e7-e52-g4 Qd8-h4"
        convertPgnToJsonPgnArray(invalidPgn)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertPgnToJsonPgnArray detects missing move numbers`() {
        println("$TAG :: Missing move numbers detection")

        val invalidPgn = "f2-f3 e7-e5 g2-g4 Qd8-h4"
        convertPgnToJsonPgnArray(invalidPgn)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertPgnToJsonPgnArray detects invalid piece notation`() {
        println("$TAG :: Invalid piece notation detection")

        val invalidPgn = "1. f2-f3 Xe7-e5"
        convertPgnToJsonPgnArray(invalidPgn)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertPgnToJsonPgnArray detects invalid square coordinates`() {
        println("$TAG :: Invalid square coordinates detection")

        val invalidPgn = "1. f2-f9 e7-e5"
        convertPgnToJsonPgnArray(invalidPgn)
    }
}
