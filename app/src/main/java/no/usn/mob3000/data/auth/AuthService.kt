package no.usn.mob3000.data.auth

import android.util.Log
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.SupabaseClientWrapper

class AuthService {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val supabase = SupabaseClientWrapper.getClient()

    fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    supabase.auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }
                }

                val user = withContext(Dispatchers.IO) {
                    supabase.auth.retrieveUserForCurrentSession(updateSession = true)
                }

                val userData = fetchUserData(user)
                onSuccess(userData)
            } catch (e: Exception) {
                Log.e("AuthService", "Error logging in", e)
                onError("Login failed: ${e.message}")
            }
        }
    }

    private suspend fun fetchUserData(user: UserInfo): User {
        val profile = withContext(Dispatchers.IO) {
            supabase.from("profiles").select() {
                filter {
                    eq("id", user.id)
                }
            }.decodeSingle<UserProfile>()
        }

        val friends = withContext(Dispatchers.IO) {
            supabase.from("friends").select(columns = Columns.list("user2")) {
                filter {
                    eq("user1", user.id)
                }
            }.decodeAs<List<User>>()
        }

        val friendRequests = withContext(Dispatchers.IO) {
            supabase.from("friend_requests").select(columns = Columns.list("by_user")) {
                filter {
                    eq("to_user", user.id)
                }
            }.decodeAs<List<User>>()
        }

        val isAdmin: Boolean = withContext(Dispatchers.IO) {
            supabase.postgrest.rpc("admin_is_admin").decodeAs<Boolean>()
        }

        val socialData = UserSocial(friends, friendRequests)

        return User(
            id = user.id,
            email = user.email,
            isAdmin = isAdmin,
            accountCreatedAt = user.createdAt,
            accountUpdatedAt = user.updatedAt,
            emailConfirmedAt = user.emailConfirmedAt,
            emailConfirmationSentAt = user.confirmationSentAt,
            recoveryEmailSentAt = user.recoverySentAt,
            lastSignInAt = user.lastSignInAt,
            profile = profile,
            socialData = socialData
        )
    }
}
