package no.usn.mob3000.domain.usecase.game.opening

import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.helper.game.mapToData
import no.usn.mob3000.domain.repository.game.IGroupsRepository
import no.usn.mob3000.domain.repository.game.IOpeningsRepository

/**
 * Android Use Case for handling chess opening deletion operations.
 *
 * @property openingsRepository The repository handling opening operations.
 * @property groupsRepository The repository handling group operations.
 * @author frigvid
 * @created 2024-11-14
 */
class DeleteOpeningUseCase(
    private val openingsRepository: IOpeningsRepository,
    private val groupsRepository: IGroupsRepository
) {
    suspend operator fun invoke(
        openingId: String
    ): Result<Unit> = try {
        Logger.d("Deleting opening with ID: $openingId")

        openingsRepository.delete(openingId)

        val groupsWithOpening = groupsRepository.getGroupsContainingOpening(openingId)

        groupsWithOpening.forEach { group ->
            val updatedOpenings = group.openings.filter { it.id != openingId }

            groupsRepository.update(
                group
                    .copy(openings = updatedOpenings)
                    .mapToData()
            )
        }

        Result.success(Unit)
    } catch (error: Exception) {
        Result.failure(error)
    }
}
