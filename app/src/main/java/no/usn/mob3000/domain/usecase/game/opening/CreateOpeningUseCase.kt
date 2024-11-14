package no.usn.mob3000.domain.usecase.game.opening

import no.usn.mob3000.domain.helper.game.mapToData
import no.usn.mob3000.domain.model.game.Opening
import no.usn.mob3000.domain.repository.game.IOpeningsRepository

/**
 * Android Use Case for handling chess opening creation operations via DTOs.
 *
 * @property openingsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-14
 */
class CreateOpeningUseCase(
    private val openingsRepository: IOpeningsRepository
) {
    suspend operator fun invoke(
        opening: Opening
    ): Result<Unit> = try {
        openingsRepository.create(opening.mapToData())
    } catch (error: Exception) {
        Result.failure(error)
    }
}
