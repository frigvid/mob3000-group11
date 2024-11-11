package no.usn.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.domain.model.auth.state.DeleteAccountState
import no.usn.mob3000.domain.model.auth.state.LogoutState
import no.usn.mob3000.domain.viewmodel.CBViewModel
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.Loading
import no.usn.mob3000.ui.components.debug.AuthenticationStatusText
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin
import no.usn.mob3000.ui.components.settings.SettingsSectionApplication
import no.usn.mob3000.ui.components.settings.SettingsSectionUser
import no.usn.mob3000.ui.components.auth.Error as ProgressError
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * SettingsScreen allows users to configure user and application-wide settings.
 *
 * TODO: Actually implement more than just the UI.
 *
 * @param logoutState The logout state.
 * @param logoutStateReset Callback function to reset the account logout state.
 * @param authenticationState The authentication status state.
 * @param onLogoutClick Function to log the user out.
 * @param onLoginClick Function to log the user in.
 * @param accountDeleteState The account deletion state.
 * @param accountDeleteStateReset Callback function to reset the account deletion state.
 * @param authenticationStateUpdate Callback function to update the authentication status state immediately.
 * @param onDeleteAccountClick Function to delete account.
 * @param onAdminDashboardClick Callback function to navigate to the administrator dashboard.
 * @param selectedTheme The currently selected theme, displayed in the theme dropdown.
 * @param selectedLanguage The currently selected language, displayed in the language dropdown.
 * @param onThemeChange Callback function invoked when the user selects a new theme. It receives
 *                      the selected theme as a String parameter.
 * @param onLanguageChange Callback function invoked when the user selects a new language. It
 *                         receives the selected language as a String parameter.
 * @see Viewport
 * @see CBViewModel
 * @author frigvid
 * @created 2024-09-12
 */
@Composable
fun SettingsScreen(
    logoutState: Flow<LogoutState>,
    logoutStateReset: () -> Unit,
    authenticationState: StateFlow<AuthenticationState>,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    accountDeleteState: Flow<DeleteAccountState>,
    accountDeleteStateReset: () -> Unit,
    authenticationStateUpdate: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onAdminDashboardClick: () -> Unit,
    selectedTheme: String,
    selectedLanguage: String,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    Viewport(
        topBarActions = { AuthenticationStatusText(authenticationState, authenticationStateUpdate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSectionAdmin(
                authenticationState,
                authenticationStateUpdate,
                onAdminDashboardClick
            )

            SettingsSectionUser(
                authenticationState,
                logoutState,
                accountDeleteState,
                logoutStateReset,
                accountDeleteStateReset,
                authenticationStateUpdate,
                onLoginClick,
                onLogoutClick,
                onDeleteAccountClick
            )

            SettingsSectionApplication(
                selectedTheme,
                selectedLanguage,
                onThemeChange,
                onLanguageChange
            )
        }
    }
}
