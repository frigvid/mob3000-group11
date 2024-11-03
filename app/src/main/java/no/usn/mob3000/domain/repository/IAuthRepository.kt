package no.usn.mob3000.domain.repository

import no.usn.mob3000.domain.model.User

/**
 * Interface for authentication repository.
 *
 * @author frigvid
 * @contributor Anarox
 * @created 2024-10-25
 */
interface IAuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun changePassword()
    suspend fun changeEmail()
    suspend fun delete()
}
