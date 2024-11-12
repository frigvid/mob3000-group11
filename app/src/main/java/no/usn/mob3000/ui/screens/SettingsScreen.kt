package no.usn.mob3000.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.domain.model.auth.state.DeleteAccountState
import no.usn.mob3000.domain.model.auth.state.LogoutState
import no.usn.mob3000.domain.viewmodel.CBViewModel
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.Loading
import no.usn.mob3000.ui.components.auth.Error as ProgressError

/**
 * SettingsScreen allows users to configure application-wide settings.
 *
 * This composable function creates a screen where users can modify the application's language
 * and theme. It uses dropdown menus for both settings, allowing users to select from predefined
 * options. The current selections are displayed and can be changed, with the changes being
 * propagated back to the ViewModel through callback functions.
 *
 * TODO: Actually implement more than just the UI.
 * TODO: Setting the values need to be changed. Currently, they'll display the hard-coded string,
 *       and not whatever localized string resource it should be. This may confuse users.
 *
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    logoutState: Flow<LogoutState>,
    logoutStateReset: () -> Unit,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    accountDeleteState: Flow<DeleteAccountState>,
    accountDeleteStateReset: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onAdminDashboardClick: () -> Unit,
    selectedTheme: String,
    selectedLanguage: String,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    val stateLogout by logoutState.collectAsState(initial = LogoutState.Idle)
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    val stateAccountDeletion by accountDeleteState.collectAsState(initial = DeleteAccountState.Idle)
    var showAccountDeletionConfirmation by remember { mutableStateOf(false) }

    var themeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

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

    if (showAccountDeletionConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.settings_section_user_popup_delete_title),
            onConfirm = {
                showAccountDeletionConfirmation = false
                onDeleteAccountClick()
            },
            onDismiss = { showAccountDeletionConfirmation = false }
        )
    }

    if (showLogoutConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.settings_section_user_popup_logout_title),
            onConfirm = {
                showLogoutConfirmation = false
                onLogoutClick()
            },
            onDismiss = { showLogoutConfirmation = false }
        )
    }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // TODO: Only show if user is admin.
            Text(
                "[ " + stringResource(R.string.settings_section_admin_subtitle) + " ]",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = onAdminDashboardClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.settings_section_admin_button_admin)) }

            Text(
                "[ " + stringResource(R.string.settings_section_user_subtitle) + " ]",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.settings_section_user_button_login)) }

            /* TODO: Only show this when the user is logged in. */
            OutlinedButton(
                onClick = { showLogoutConfirmation = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.settings_section_user_button_logout) + "?") }

            when (stateLogout) {
                is LogoutState.Success -> {
                    ProgressError(
                        text = stringResource(R.string.settings_section_user_state_logout_success),
                        cardContainerColor = Color.Green
                    )
                }

                is LogoutState.Error -> {
                    ProgressError(
                        text = "Given how logout works, this should never be shown. So, well, if you're seeing this ... take this star as a reward: \uD83C\uDF1F"
                    )
                }

                is LogoutState.Loading -> Loading()

                else -> {  }
            }

            /* TODO: Add button to delete user. */
            Text(
                "Once pressed, this will open a dialogue to ensure you wanted this. If you accept that, it's an immediate, unrecoverable deletion."
            )
            Button(
                onClick = { showAccountDeletionConfirmation = true },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.settings_section_user_button_delete)) }

            when (stateAccountDeletion) {
                is DeleteAccountState.Success -> {
                    ProgressError(
                        text = stringResource(R.string.settings_section_user_state_delete_success),
                        cardContainerColor = Color.Green
                    )
                }

                is DeleteAccountState.Error -> {
                    ProgressError(
                        text = stringResource(R.string.settings_section_user_state_delete_error)
                    )
                }

                is DeleteAccountState.Loading -> Loading()

                else -> {  }
            }

            Text(
                "[ " + stringResource(R.string.settings_section_app_subtitle) + " ]",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                stringResource(R.string.settings_section_app_language),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = !languageExpanded }
            ) {
                TextField(
                    value = selectedLanguage,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                       .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_section_app_language_enus), color = MaterialTheme.colorScheme.onBackground) },
                        onClick = {
                            onLanguageChange("English")
                            languageExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_section_app_language_nonb), color = MaterialTheme.colorScheme.onBackground) },
                        onClick = {
                            onLanguageChange("Norwegian bokmål")
                            languageExpanded = false
                        }
                    )
                }
            }

            Text(
                stringResource(R.string.settings_section_app_theme),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            ExposedDropdownMenuBox(
                expanded = themeExpanded,
                onExpandedChange = { themeExpanded = !themeExpanded }
            ) {
                TextField(
                    value = selectedTheme,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                       .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = themeExpanded,
                    onDismissRequest = { themeExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_section_app_theme_light), color = MaterialTheme.colorScheme.onBackground) },
                        onClick = {
                            onThemeChange("Default - light")
                            themeExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_section_app_theme_dark), color = MaterialTheme.colorScheme.onBackground) },
                        onClick = {
                            onThemeChange("Default - dark")
                            themeExpanded = false
                        }
                    )
                }
            }
        }
    }
}
