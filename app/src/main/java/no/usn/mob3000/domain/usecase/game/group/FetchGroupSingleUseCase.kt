package no.usn.mob3000.domain.usecase.game.group

import no.usn.mob3000.domain.model.game.group.Group
import no.usn.mob3000.domain.repository.game.IGroupsRepository

/**
 * Android Use Case for fetching a single chess repertoire/group.
 *
 * @property groupsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-15
 */
class FetchGroupSingleUseCase(
    private val groupsRepository: IGroupsRepository
) {
    suspend operator fun invoke(
        groupId: String
    ): Result<Group> = try {
        Result.success(groupsRepository.getGroupSingle(groupId))
    } catch (error: Exception) {
        Result.failure(error)
    }
}
