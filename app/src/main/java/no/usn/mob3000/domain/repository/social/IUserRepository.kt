package no.usn.mob3000.domain.repository.social

import no.usn.mob3000.domain.model.auth.UserProfile

/**
 * TODO: Kdoc
 *
 * @author 258030
 * @created 2024-11-09
 */
interface IUserRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfile?>
    suspend fun fetchUserById(userId: String): Result<UserProfile?>

}
