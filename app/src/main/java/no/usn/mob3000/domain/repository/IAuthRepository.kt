package no.usn.mob3000.domain.repository

import no.usn.mob3000.domain.model.auth.User

/**
 * Interface for authentication repository.
 *
 * @author frigvid
 * @contributor Anarox1111
 * @created 2024-10-25
 */
interface IAuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun changePassword(newPassword: String): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun changeEmail(newEmail: String): Result<Unit>
    suspend fun delete()
    suspend fun getCurrentUserId(): String
    suspend fun importSessionToken(sessionToken: String)
}
