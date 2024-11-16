package no.usn.mob3000.domain.usecase.game.opening

import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.repository.game.IOpeningsRepository

/**
 * Android Use Case for fetching a single chess opening.
 *
 * @property openingsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-14
 */
class FetchOpeningSingleUseCase(
    private val openingsRepository: IOpeningsRepository
) {
    suspend operator fun invoke(
        openingId: String
    ): Result<Opening> = try {
        Result.success(openingsRepository.getOpeningSingle(openingId))
    } catch (error: Exception) {
        Result.failure(error)
    }
}
