package no.usn.mob3000.domain.usecase.game.group

import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.repository.game.IGroupsRepository

/**
 * Android Use Case for handling chess repertoire/group deletion operations.
 *
 * @property groupsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-15
 */
class DeleteGroupUseCase(
    private val groupsRepository: IGroupsRepository
) {
    suspend operator fun invoke(
        groupId: String
    ): Result<Unit> = try {
        Logger.d("Deleting group with ID: $groupId")
        groupsRepository.delete(groupId)
        Result.success(Unit)
    } catch (error: Exception) {
        Result.failure(error)
    }
}
