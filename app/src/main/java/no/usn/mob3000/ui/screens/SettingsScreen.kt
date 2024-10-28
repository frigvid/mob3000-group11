package no.usn.mob3000.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.domain.viewmodel.CBViewModel
import no.usn.mob3000.ui.theme.DefaultButton

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
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAdminDashboardClick: () -> Unit,
    selectedTheme: String,
    selectedLanguage: String,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    var themeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // TODO: Only show if user is admin.
            Text(
                "[ ADMIN ]",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = onAdminDashboardClick,
                colors = ButtonDefaults.buttonColors(DefaultButton),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text("GO TO ADMIN DASHBOARD") }

            Text(
                "[ USER ]",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(DefaultButton),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text("GO TO LOGIN") }

            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text("LOG OUT") }

            Text(
                "[ APPLICATION ]",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                stringResource(R.string.settings_language),
                style = MaterialTheme.typography.titleMedium
            )

            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = !languageExpanded }
            ) {
                TextField(
                    value = selectedLanguage,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                       .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_language_enus)) },
                        onClick = {
                            onLanguageChange("English")
                            languageExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_language_nonb)) },
                        onClick = {
                            onLanguageChange("Norwegian bokmål")
                            languageExpanded = false
                        }
                    )
                }
            }

            Text(
                stringResource(R.string.settings_theme),
                style = MaterialTheme.typography.titleMedium
            )

            ExposedDropdownMenuBox(
                expanded = themeExpanded,
                onExpandedChange = { themeExpanded = !themeExpanded }
            ) {
                TextField(
                    value = selectedTheme,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                       .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = themeExpanded,
                    onDismissRequest = { themeExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_theme_light)) },
                        onClick = {
                            onThemeChange("Default - light")
                            themeExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.settings_theme_dark)) },
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
