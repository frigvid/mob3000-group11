package no.usn.mob3000.ui.screens.auth

import androidx.compose.runtime.Composable
import no.usn.mob3000.domain.model.auth.state.ChangePasswordState
import no.usn.mob3000.ui.components.auth.PasswordResetContent

/**
 * This is the password reset screen, where users can change their passwords.
 *
 * @param onResetPasswordClick Callback triggered when the user presses the "Reset Password"
 *                             button to initiate the password reset process.
 * @param changePasswordStateUpdate Callback to update password change state
 * @param authenticationStateUpdate Callback to update authentication state
 * @param navControllerPopBackStack Callback to handle back navigation
 * @author Markus, frigvid, Anarox
 * @created 2024-09-24
 */
@Composable
fun ResetPasswordScreen(
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
