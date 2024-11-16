package no.usn.mob3000.domain.usecase.game.opening

import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.repository.game.IOpeningsRepository

/**
 * Android Use Case for handling chess opening fetching operations.
 *
 * @property openingsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-14
 */
class FetchOpeningsUseCase(
    private val openingsRepository: IOpeningsRepository
) {
    suspend operator fun invoke(): Result<List<Opening>> = try {
        Result.success(openingsRepository.getOpenings())
    } catch (error: Exception) {
        Result.failure(error)
    }
}
