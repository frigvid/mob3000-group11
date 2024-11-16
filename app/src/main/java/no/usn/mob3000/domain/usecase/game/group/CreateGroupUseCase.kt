package no.usn.mob3000.domain.usecase.game.group

import no.usn.mob3000.domain.helper.game.mapToData
import no.usn.mob3000.domain.model.game.Group
import no.usn.mob3000.domain.repository.game.IGroupsRepository

/**
 * Android Use Case for handling chess repertoire/group creation operations via DTOs.
 *
 * @property groupsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-15
 */
class CreateGroupUseCase(
    private val groupsRepository: IGroupsRepository
) {
    suspend operator fun invoke(
        group: Group
    ): Result<Unit> = try {
        groupsRepository.create(group.mapToData())
    } catch (error: Exception) {
        Result.failure(error)
    }
}
