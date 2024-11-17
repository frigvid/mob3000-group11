package no.usn.mob3000.domain.source.game

import no.usn.mob3000.data.model.game.RepertoireDto

/**
 * Interface for the repertoire/group data source.
 *
 * @author frigvid
 * @created 2024-11-15
 */
interface IGroupsDataSource {
    suspend fun getGroups(): List<RepertoireDto>
    suspend fun getGroupSingle(groupId: String): RepertoireDto
}
