package no.usn.mob3000.ui.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import no.usn.mob3000.data.auth.User

/**
 * ViewModel to user state and, if necessary, authentication
 * state.
 *
 * @author frigvid
 * @created 2024-10-21
 */
class AuthViewModel : ViewModel() {
    private val _authenticatedUser = mutableStateOf<User?>(null)
    val authenticatedUser: State<User?> = _authenticatedUser

    fun setAuthenticatedUser(user: User) {
        _authenticatedUser.value = user
    }
}