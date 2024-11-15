package no.usn.mob3000.domain.helper.game

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import no.usn.mob3000.data.model.game.RepertoireDto
import no.usn.mob3000.domain.model.game.Group

/**
 * Extension function to map a DTO repertoire to a pure kotlin data class repertoire/group.
 *
 * @author frigvid
 * @created 2024-11-15
 */
fun RepertoireDto.mapToDomain(): Group = Group(
    id = this.repertoireId,
    createdBy = this.createdBy,
    title = this.title,
    description = this.description,
    openings = mapOpeningIdsToOpenings(Json.decodeFromString(this.openings.toString())),
    createdAt = this.createdAt
)

/**
 * Extension function to map an pure kotlin data class repertoire/group to a DTO repertoire.
 *
 * @author frigvid
 * @created 2024-11-15
 */
fun Group.mapToData(): RepertoireDto = RepertoireDto(
    repertoireId = this.id,
    createdAt = this.createdAt,
    createdBy = this.createdBy,
    openings = JsonArray(
        this.openings.map {
            buildJsonObject {
                put("id", it.id)
            }
        }
    ),
    title = this.title ?: "",
    description = this.description
)
