package no.usn.mob3000.domain.repository.social

import no.usn.mob3000.domain.model.auth.UserGameStats
import no.usn.mob3000.domain.model.auth.UserProfile

/**
 * Interface for the user repository.
 *
 * @author 258030
 * @created 2024-11-09
 */
interface IUserRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfile?>
    suspend fun fetchUserById(userId: String): Result<UserProfile?>
    suspend fun getUserGameStats(): Result<UserGameStats>
}
