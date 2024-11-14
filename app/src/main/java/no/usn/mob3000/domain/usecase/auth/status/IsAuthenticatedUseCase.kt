package no.usn.mob3000.domain.usecase.auth.status

import io.github.jan.supabase.gotrue.user.UserInfo
import no.usn.mob3000.data.source.remote.auth.UserDataSource

/**
 * Check whether the user is authenticated.
 *
 * @author frigvid
 * @created 2024-11-07
 */
class IsAuthenticatedUseCase(
    private val userDataSource: UserDataSource = UserDataSource()
) {
    suspend operator fun invoke(): Result<UserInfo?> = try {
        Result.success(userDataSource.getCurrentUser())
    } catch (error: Exception) {
        Result.failure(error)
    }
}
