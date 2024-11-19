package no.usn.mob3000.domain.source

import io.github.jan.supabase.gotrue.user.UserSession

/**
 * Interface for authentication data source.
 *
 * @author frigvid
 * @created 2024-10-28
 */
interface IAuthDataSource {
    suspend fun getCurrentSession(): UserSession?
}
