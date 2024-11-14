package no.usn.mob3000.domain.usecase.game.opening

import android.util.Log
import no.usn.mob3000.domain.repository.game.IOpeningsRepository

/**
 * Android Use Case for handling chess opening deletion operations.
 *
 * @property openingsRepository The repository handling opening operations.
 * @author frigvid
 * @created 2024-11-14
 */
class DeleteOpeningUseCase(
    private val openingsRepository: IOpeningsRepository
) {
    suspend operator fun invoke(
        openingId: String
    ): Result<Unit> = try {
        Log.d("DeleteOpeningUseCase", "Deleting opening with ID: $openingId")
        openingsRepository.delete(openingId)
        Result.success(Unit)
    } catch (error: Exception) {
        Result.failure(error)
    }
}
