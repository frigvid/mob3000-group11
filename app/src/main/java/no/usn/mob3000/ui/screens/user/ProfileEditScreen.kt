package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ConfirmationDialog
import no.usn.mob3000.ui.components.socials.friendrequests.profileConfirmDialog
import no.usn.mob3000.ui.theme.DefaultButton
/**
 * Screen to allow users to edit their profiles, and get access to some user-related settings.
 *
 * @author frigvid
 * @contributor 258030
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
                placeholder = { Text(stringResource(R.string.profile_edit_avatar_url_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text(stringResource(R.string.profile_edit_display_name)) },
                placeholder = { Text(stringResource(R.string.profile_edit_display_name_placeholder)) },
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
                    label = { Text(stringResource(R.string.profile_edit_nationality)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    commonCountries.forEach { (code, name) ->
                        DropdownMenuItem(
                            text = { Text(name) },
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
                    colors = SwitchDefaults.colors(checkedTrackColor = DefaultButton)
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
                    colors = SwitchDefaults.colors(checkedTrackColor = DefaultButton)
                )
            }

            Button(
                onClick = {
                    showDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(DefaultButton),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { Text(stringResource(R.string.profile_edit_save_button)) }
        }
    }
    if (showDialog.value) {
        profileConfirmDialog(
            showDialog = showDialog,
            onConfirm = {
                onSaveProfileClick(displayName, avatarUrl, aboutMe, selectedCountry, isProfileVisible, isFriendsListVisible)
                navigateToProfile()
            },
            onDismiss = {
                // Close the dialog when dismissed
                showDialog.value = false
            },
            "Save changes?","do you want to save your changes?","Save","Cancel"
        )
    }
}
