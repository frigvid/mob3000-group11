package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.social.ProfileConfirmDialog

/**
 * Screen to allow users to edit their profiles, and get access to some user-related settings.
 *
 * @author frigvid
 * @contributor 258030, Husseinabdulameer11
 * @created 2024-10-11
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    selectedUser: UserProfile? = null,
    onSaveProfileClick: (String, String, String, String, Boolean, Boolean) -> Unit,
    navigateToProfile: () -> Unit
) {
    var avatarUrl by remember { mutableStateOf(selectedUser?.avatarUrl ?: "") }
    var displayName by remember { mutableStateOf(selectedUser?.displayName ?: "") }
    var aboutMe by remember { mutableStateOf(selectedUser?.aboutMe ?: "") }
    var selectedCountry by remember { mutableStateOf(selectedUser?.nationality ?: "") }
    var isProfileVisible by remember { mutableStateOf(selectedUser?.visibility ?: true) }
    var isFriendsListVisible by remember { mutableStateOf(selectedUser?.visibilityFriends ?: true) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

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
                placeholder = {
                    Text(
                        stringResource(R.string.profile_edit_avatar_url_placeholder),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                },
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


            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text(stringResource(R.string.profile_edit_display_name)) },
                placeholder = {
                    Text(
                        stringResource(R.string.profile_edit_display_name_placeholder),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = aboutMe,
                onValueChange = { aboutMe = it },
                label = { Text(stringResource(R.string.profile_edit_about_me)) },
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
                    commonCountries.forEach { (_, name) ->
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
                onClick = { showDialog.value = true },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(stringResource(R.string.profile_edit_save_button))
            }
        }
    }

    if (showDialog.value) {
        ProfileConfirmDialog(
            showDialog = showDialog,
            onConfirm = {
                onSaveProfileClick(
                    displayName,
                    avatarUrl,
                    aboutMe,
                    selectedCountry,
                    isProfileVisible,
                    isFriendsListVisible
                )
                navigateToProfile()
            },
            onDismiss = { showDialog.value = false },
            stringResource(R.string.profile_component_profile_edit_title),
            stringResource(R.string.profile_component_profile_edit_text),
            stringResource(R.string.profile_component_profile_edit_confirm),
            stringResource(R.string.profile_component_profile_edit_dismiss)
        )
    }
}
