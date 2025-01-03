package no.usn.mob3000.domain.usecase.social

import no.usn.mob3000.data.repository.social.UserRepository
import no.usn.mob3000.domain.model.auth.UserGameStats

/**
 * Fetch the user's game stats.
 *
 * @author 258030
 * @created 2024-11-17
 */
class GetUserGameStatsUseCase (
    private val fetchRepository: UserRepository = UserRepository()
) {
    /**
     * Get the user's game stats.
     */
    suspend operator fun invoke(): Result<UserGameStats> {
        return fetchRepository.getUserGameStats()
    }
}
