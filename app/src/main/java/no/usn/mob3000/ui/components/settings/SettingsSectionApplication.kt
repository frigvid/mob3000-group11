package no.usn.mob3000.ui.components.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R

/**
 * Application section for the `SettingsScreen`.
 *
 * This composable function is where users can modify the application's language and theme.
 * It uses dropdown menus for both settings, allowing users to select from predefined options.
 * The current selections are displayed and can be changed, with the changes being propagated
 * back to the ViewModel through callback functions.
 *
 * TODO: Setting the values need to be changed. Currently, they'll display the hard-coded string,
 *       and not whatever localized string resource it should be. This may confuse users.
 *
 * @param selectedTheme The currently selected theme, displayed in the theme dropdown.
 * @param selectedLanguage The currently selected language, displayed in the language dropdown.
 * @param onThemeChange Callback function invoked when the user selects a new theme. It receives
 *                      the selected theme as a String parameter.
 * @param onLanguageChange Callback function invoked when the user selects a new language. It
 *                         receives the selected language as a String parameter.
 * @author frigvid
 * @created 2024-09-12
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSectionApplication(
    selectedTheme: String,
    selectedLanguage: String,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    var themeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

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
                text = { Text(stringResource(R.string.settings_section_app_language_enus)) },
                onClick = {
                    onLanguageChange("English")
                    languageExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.settings_section_app_language_nonb)) },
                onClick = {
                    onLanguageChange("Norwegian bokmål")
                    languageExpanded = false
                }
            )
        }
    }

    Text(
        stringResource(R.string.settings_section_app_theme),
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
                text = { Text(stringResource(R.string.settings_section_app_theme_light)) },
                onClick = {
                    onThemeChange("Default - light")
                    themeExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.settings_section_app_theme_dark)) },
                onClick = {
                    onThemeChange("Default - dark")
                    themeExpanded = false
                }
            )
        }
    }
}
