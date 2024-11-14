package no.usn.mob3000.game

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import no.usn.mob3000.domain.helper.game.convertJsonPgnArrayToPgn
import org.junit.Test
import org.junit.Assert.*

class PgnConversionUnitTest {
    private val TAG: String = "> Test :app:PgnConversionUnitTest"

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

    @Test
    fun `convertJsonPgnArrayToPgn converts valid JSON PGN array`() {
        println("$TAG :: Valid JSON PGN array parsing")

        val jsonArray = Json.decodeFromString<JsonArray>(validJsonPgnArray)
        val result = convertJsonPgnArrayToPgn(jsonArray)

        println("> Result: $result")

        assertEquals("1. f2-f3 e7-e5 2. g2-g4 Qd8-h4", result)
    }

    @Test
    fun `convertJsonPgnArrayToPgn converts malformed JSON PGN array`() {
        println("$TAG :: Malformed JSON PGN array parsing")

        val jsonArray = Json.decodeFromString<JsonArray>(malformedJsonPgnArray)

        assertThrows(Exception::class.java) {
            convertJsonPgnArrayToPgn(jsonArray)
        }
    }

    @Test
    fun `convertJsonPgnArrayToPgn handles null input`() {
        println("$TAG :: Null input parsing")

        val result = convertJsonPgnArrayToPgn(null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `convertJsonPgnArrayToPgn throws exception for invalid piece type`() {
        println("$TAG :: Malformed JSON PGN array parsing")

        val jsonArray = Json.decodeFromString<JsonArray>("""[{"to": "f3", "from": "f2", "piece": "x"}]""")
        assertThrows(Exception::class.java) {
            convertJsonPgnArrayToPgn(jsonArray)
        }
    }
}
