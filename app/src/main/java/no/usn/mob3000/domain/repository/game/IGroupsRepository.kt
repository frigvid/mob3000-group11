package no.usn.mob3000.domain.repository.game

import no.usn.mob3000.data.model.game.RepertoireDto
import no.usn.mob3000.domain.model.game.group.Group

/**
 * Interface for the repertoire/group repository.
 *
 * @author frigvid
 * @created 2024-11-15
 */
interface IGroupsRepository {
    suspend fun create(group: RepertoireDto): Result<Unit>
    suspend fun delete(groupId: String)
    suspend fun update(group: RepertoireDto): Result<Unit>
    suspend fun getGroups(): List<Group>
    suspend fun getGroupSingle(groupId: String): Group
    suspend fun getGroupsContainingOpening(openingId: String): List<Group>
}
