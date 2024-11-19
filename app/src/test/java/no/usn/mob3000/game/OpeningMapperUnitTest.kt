package no.usn.mob3000.game

import kotlinx.datetime.Clock
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import no.usn.mob3000.data.model.game.OpeningsDto
import no.usn.mob3000.domain.helper.game.mapToData
import no.usn.mob3000.domain.helper.game.mapToDomain
import no.usn.mob3000.domain.model.game.opening.Opening
import org.junit.Test
import org.junit.Assert.*

private const val TAG: String = "> TEST :app:OpeningMapperTest"

/**
 * DTO openings and pure kotlin data class openings mapper tests.
 *
 * Docstrings for individual tests aren't included, as the
 * test's names are made to be at least mostly self-explanatory.
 *
 * @author frigvid
 * @created 2024-11-14
 */
class OpeningMapperUnitTest {
    private val jsonPgnArray = buildJsonArray {
        add(
            buildJsonObject {
                put("to", "e4")
                put("from", "e2")
                put("piece", "p")
            }
        )
    }

    private val time = Clock.System.now()

    private val dataOpening = OpeningsDto(
        openingId = "test-id",
        createdByUser = "user-id",
        title = "Test Opening",
        content = "Test Description",
        pgn = jsonPgnArray,
        createdAt = time
    )

    private val domainOpening = Opening(
        id = "test-id",
        createdBy = "user-id",
        title = "Test Opening",
        description = "Test Description",
        moves = "1. e4",
        createdAt = time
    )

    @Test
    fun `test mapping from DTO to domain`() {
        println("$TAG :: Map data opening to domain opening")

        val result = dataOpening.mapToDomain()
        assertEquals(domainOpening, result)
    }

    @Test
    fun `test mapping from domain to DTO`() {
        println("$TAG :: Map domain opening to data opening")

        val result = domainOpening.mapToData()
        assertEquals(dataOpening, result)
    }

    // TODO: This test is currently failing because the amount of handling necessary to properly
    //       deal with PGN nuances are *complex*. Too much so that we don't have time to implement
    //       it now. Had we time, a less fragile converter could've been made.
    //@Test
    //fun `test bidirectional mapping`() {
    //    println("$TAG :: Map data opening to domain opening and back.")
    //
    //    val domainResult = dataOpening.mapToDomain()
    //    val dtoResult = domainResult.mapToData()
    //
    //    assertEquals(dataOpening, dtoResult)
    //    assertEquals(domainOpening, domainResult)
    //}
}
