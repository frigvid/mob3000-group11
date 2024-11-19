package no.usn.mob3000.ui.components.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.auth.state.ChangeEmailState
import no.usn.mob3000.domain.model.auth.state.ChangePasswordState
import no.usn.mob3000.domain.model.auth.state.DeleteAccountState
import no.usn.mob3000.domain.model.auth.state.LogoutState
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.Loading
import no.usn.mob3000.ui.components.auth.Error

private const val TAG: String = "SettingsSectionUser"

/**
 * Composable component for the `SettingsScreen` user section.
 *
 * @param authenticationState The authentication status state.
 * @param logoutState The logout state.
 * @param accountDeleteState The account deletion state.
 * @param logoutStateReset Callback function to reset the account logout state.
 * @param accountDeleteStateReset Callback function to reset the account deletion state.
 * @param authenticationStateUpdate Callback function to update the authentication status state immediately.
 * @param onLogoutClick Function to log the user out.
 * @param onLoginClick Function to log the user in.
 * @param onDeleteAccountClick Function to delete account.
 * @param navigateToPasswordReset Callback function to navigate to the password reset screen.
 * @param navigateToEmailChange Callback function to navigate to the e-mail address change screen.
 * @author frigvid
 * @created 2024-11-11
 */
@Composable
fun SettingsSectionUser(
    authenticationState: StateFlow<AuthenticationState>,
    logoutState: Flow<LogoutState>,
    accountDeleteState: Flow<DeleteAccountState>,
    changeEmailState: Flow<ChangeEmailState>,
    changePasswordState: Flow<ChangePasswordState>,
    logoutStateReset: () -> Unit,
    accountDeleteStateReset: () -> Unit,
    authenticationStateUpdate: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    navigateToPasswordReset: () -> Unit,
    navigateToEmailChange: () -> Unit
) {
    /**
     * This is sort of viscerally disgusting, but also somewhat necessary.
     *
     * Without this, any successes and error dialogues will be shown indefinitely,
     * which *can* be handled with a timer or some such, but honestly? This is just
     * much easier, and works practically just as well for less complexity.
     *
     * So as much as this disgusts me, it's here to stay.
     *
     * For now, at least.
     *
     * @author frigvid
     */
    logoutStateReset()
    accountDeleteStateReset()

    /**
     * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-11
     */
    val state by remember { authenticationState }.collectAsState()
    LaunchedEffect(Unit) { authenticationStateUpdate() }

    Text(
        "[ " + stringResource(R.string.settings_section_user_subtitle) + " ]",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    when (state) {
        is AuthenticationState.Error,
        is AuthenticationState.Unauthenticated -> {
            Logger.d("User is not authenticated. Showing login button.")
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.settings_section_user_button_login)) }
        }

        is AuthenticationState.Authenticated -> {
            Logger.d("User is authenticated. Showing authenticated user actions.")

            LogoutAccount(logoutState, onLogoutClick, authenticationStateUpdate)
            EmailChange(navigateToEmailChange, changeEmailState)
            PasswordChange(navigateToPasswordReset, changePasswordState)
            DeleteAccount(accountDeleteState, onDeleteAccountClick, authenticationStateUpdate)
        }

        else -> Logger.d("Something went wrong.")
    }
}

/**
 * Private composable component to display and handle account logout.
 *
 * @param logoutState The logout state.
 * @param onLogoutClick Function to log the user out.
 * @param authenticationStateUpdate Function to immediately schedule an authentication state update.
 * @author frigvid
 * @created 2024-11-11
 */
@Composable
private fun LogoutAccount(
    logoutState: Flow<LogoutState>,
    onLogoutClick: () -> Unit,
    authenticationStateUpdate: () -> Unit
) {
    val accountLogoutState by logoutState.collectAsState(initial = LogoutState.Idle)
    var showLogoutConfirmation by remember { mutableStateOf(false) }

    if (showLogoutConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.settings_section_user_popup_logout_title),
            onConfirm = {
                showLogoutConfirmation = false
                onLogoutClick()
                authenticationStateUpdate()
            },
            onDismiss = { showLogoutConfirmation = false }
        )
    }

    OutlinedButton(
        onClick = { showLogoutConfirmation = true },
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) { Text(stringResource(R.string.settings_section_user_button_logout) + "?") }

    when (accountLogoutState) {
        is LogoutState.Success -> {
            Error(
                text = stringResource(R.string.settings_section_user_state_logout_success),
                cardContainerColor = Color.Green
            )
        }

        is LogoutState.Error -> {
            Error(text = "Given how logout works, this should never be shown. So, well, if you're seeing this ... take this star as a reward: \uD83C\uDF1F")
        }

        is LogoutState.Loading -> Loading()

        else -> {}
    }
}

/**
 * Private composable component to display and handle e-mail address changes.
 *
 * @param navigationGoToChangeEmail Callback function to navigate to the e-mail address change screen.
 * @author frigvid
 * @created 2024-11-13
 */
@Composable
private fun EmailChange(
    navigationGoToChangeEmail: () -> Unit,
    changeEmailState: Flow<ChangeEmailState>
) {
    val state by changeEmailState.collectAsState(initial = ChangeEmailState.Idle)

    Button(
        onClick = navigationGoToChangeEmail,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(stringResource(R.string.settings_section_user_email_change_title))
    }

    when (state) {
        is ChangeEmailState.Success -> {
            Error(
                text = stringResource(R.string.auth_email_state_success),
                cardContainerColor = Color.Green
            )
        }

        is ChangeEmailState.Error -> {
            Error(
                text = stringResource(R.string.auth_email_state_failure)
            )
        }

        is ChangeEmailState.Loading -> Loading()

        else -> {}
    }
}

/**
 * Private composable component to display and handle password changes.
 *
 * @param navigationGoToResetPassword Callback function to navigate to the password reset screen.
 * @param changePasswordState The password change state.
 * @author frigvid
 * @created 2024-11-13
 */
@Composable
private fun PasswordChange(
    navigationGoToResetPassword: () -> Unit,
    changePasswordState: Flow<ChangePasswordState>
) {
    val state by changePasswordState.collectAsState(initial = ChangePasswordState.Idle)

    Button(
        onClick = navigationGoToResetPassword,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(stringResource(R.string.settings_section_user_password_change_title))
    }

    when (state) {
        is ChangePasswordState.Success -> {
            Error(
                text = stringResource(R.string.auth_reset_password_state_success),
                cardContainerColor = Color.Green
            )
        }

        is ChangePasswordState.Error -> {
            Error(
                text = stringResource(R.string.auth_reset_password_state_failure)
            )
        }

        is ChangePasswordState.Loading -> Loading()

        else -> {}
    }
}

/**
 * Private composable component to display and handle account deletions.
 *
 * @param accountDeleteState The account deletion state.
 * @param onDeleteAccountClick Function to delete the user's account.
 * @param authenticationStateUpdate Function to immediately schedule an authentication state update.
 * @author frigvid
 * @created 2024-11-11
 */
@Composable
private fun DeleteAccount(
    accountDeleteState: Flow<DeleteAccountState>,
    onDeleteAccountClick: () -> Unit,
    authenticationStateUpdate: () -> Unit
) {
    val accountDeletionState by accountDeleteState.collectAsState(initial = DeleteAccountState.Idle)
    var showAccountDeletionConfirmation by remember { mutableStateOf(false) }

    if (showAccountDeletionConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.settings_section_user_popup_delete_title),
            onConfirm = {
                showAccountDeletionConfirmation = false
                onDeleteAccountClick()
                authenticationStateUpdate()
            },
            onDismiss = { showAccountDeletionConfirmation = false }
        )
    }

    Text("Once pressed, this will open a dialogue to ensure you wanted this. If you accept that, it's an immediate, unrecoverable deletion.")

    Button(
        onClick = { showAccountDeletionConfirmation = true },
        colors = ButtonDefaults.buttonColors(Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) { Text(stringResource(R.string.settings_section_user_button_delete)) }

    when (accountDeletionState) {
        is DeleteAccountState.Success -> {
            Error(
                text = stringResource(R.string.settings_section_user_state_delete_success),
                cardContainerColor = Color.Green
            )
        }

        is DeleteAccountState.Error -> {
            Error(
                text = stringResource(R.string.settings_section_user_state_delete_error)
            )
        }

        is DeleteAccountState.Loading -> Loading()

        else -> {}
    }
}
