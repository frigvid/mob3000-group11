package no.usn.mob3000.ui.screens.auth

import androidx.compose.runtime.Composable
import no.usn.mob3000.domain.model.auth.state.ChangePasswordState
import no.usn.mob3000.ui.components.auth.PasswordResetContent

/**
 * This is the password reset screen that gets called when opening through email deeplink.
 *
 * @param tokenHash The token hash from the deep link
 * @param type The type of reset from the deep link
 * @param next The next action from the deep link
 * @param onResetPasswordClick Callback triggered when the reset password action is confirmed
 * @param changePasswordStateUpdate Callback to update password change state
 * @param authenticationStateUpdate Callback to update authentication state
 * @param navControllerPopBackStack Callback to handle back navigation
 * @author Anarox
 * @created 2024-11-18
 */
@Composable
fun ResetPasswordViaEmailScreen(
    tokenHash: String,
    type: String,
    next: String,
    onResetPasswordClick: (String) -> Unit,
    changePasswordStateUpdate: (ChangePasswordState) -> Unit,
    authenticationStateUpdate: () -> Unit,
    navControllerPopBackStack: () -> Unit
) {
    PasswordResetContent(
        onPasswordReset = onResetPasswordClick,
        onChangePasswordStateUpdate = changePasswordStateUpdate,
        onAuthenticationStateUpdate = authenticationStateUpdate,
        onNavigateBack = navControllerPopBackStack
    )
}
