package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport

/**
 * Screen to allow users to edit their profiles, and get access to some user-related settings.
 *
 * TODO: Implement caching of state in ViewModel for the editor.
 * TODO: Implement fetcher in data layer.
 *
 * @author frigvid
 * @created 2024-10-11
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onSaveProfileClick: () -> Unit
) {
    var avatarUrl by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var aboutMe by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var isProfileVisible by remember { mutableStateOf(true) }
    var isFriendsListVisible by remember { mutableStateOf(true) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    /* TODO: Use "https://restcountries.com/v3.1/all?fields=name,cca2" to fetch countries,
     *       and cache it in ViewModel state.
     */
    val commonCountries = listOf(
        "US" to "United States",
        "CN" to "China",
        "IN" to "India",
        "GB" to "United Kingdom",
        "DE" to "Germany",
        "FR" to "France",
        "JP" to "Japan",
        "BR" to "Brazil",
        "RU" to "Russia",
        "CA" to "Canada",
        "AU" to "Australia",
        "IT" to "Italy",
        "ES" to "Spain",
        "MX" to "Mexico",
        "KR" to "South Korea",
        "ID" to "Indonesia",
        "TR" to "Turkey",
        "SA" to "Saudi Arabia",
        "CH" to "Switzerland",
        "NL" to "Netherlands"
    )

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = avatarUrl,
                onValueChange = { avatarUrl = it },
                label = { Text(stringResource(R.string.profile_edit_avatar_url)) },
                placeholder = { Text(stringResource(R.string.profile_edit_avatar_url_placeholder), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)) },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text(stringResource(R.string.profile_edit_display_name)) },
                placeholder = { Text(stringResource(R.string.profile_edit_display_name_placeholder), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)) },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = aboutMe,
                onValueChange = { aboutMe = it },
                label = {
                    Text(stringResource(R.string.profile_edit_about_me))
                },
                placeholder = {
                    Text(
                        stringResource(R.string.profile_edit_about_me_placeholder),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
            )

            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = { selectedCountry = it },
                    readOnly = true,
                    label = {
                        Text(stringResource(R.string.profile_edit_nationality))
                    },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    /* TODO: Use country-code to decide on which emoji to display. */
                    commonCountries.forEach { (code, name) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    name,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            onClick = {
                                selectedCountry = name
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.profile_edit_visibility),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = isProfileVisible,
                    onCheckedChange = { isProfileVisible = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.profile_edit_friends_list_visibility),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = isFriendsListVisible,
                    onCheckedChange = { isFriendsListVisible = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                )
            }

            Button(
                onClick = {
                    /* TODO: Implement update to database. */
                    onSaveProfileClick()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.profile_edit_save_button)) }
        }
    }
}
