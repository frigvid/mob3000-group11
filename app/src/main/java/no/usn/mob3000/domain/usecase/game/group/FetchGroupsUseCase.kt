package no.usn.mob3000.domain.usecase.game.group

import no.usn.mob3000.domain.model.game.Group
import no.usn.mob3000.domain.repository.game.IGroupsRepository

/**
 * Android Use Case for handling chess repertoire/group fetching operations.
 *
 * @property groupsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-15
 */
class FetchGroupsUseCase(
    private val groupsRepository: IGroupsRepository
) {
    suspend operator fun invoke(): Result<List<Group>> = try {
        Result.success(groupsRepository.getGroups())
    } catch (error: Exception) {
        Result.failure(error)
    }
}
