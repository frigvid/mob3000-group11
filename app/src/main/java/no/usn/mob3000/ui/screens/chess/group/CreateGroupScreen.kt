package no.usn.mob3000.ui.screens.chess.group

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.group.Group
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.game.group.GroupEditor
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin

/**
 * Screen to allow users to create opening groups/repertoires.
 *
 * This screen allows users to input a title and description for their group,
 * select from available openings, and add them to the group. It provides buttons
 * for saving the group and clearing the inputs.
 *
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update the authentication status state.
 * @param availableOpenings List of all available openings to choose from.
 * @param onCreateGroup Callback function to create a repertoire/group.
 * @param navControllerPopBackStack Pop the navigation controller's back stack.
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun CreateGroupScreen(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    availableOpenings: List<Opening>,
    onCreateGroup: (Group) -> Unit,
    navControllerPopBackStack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedOpenings by remember { mutableStateOf(listOf<Opening>()) }

    /**
     * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    val authState by remember { authenticationState }.collectAsState()
    LaunchedEffect(Unit) { authenticationStateUpdate() }

    GroupEditor(
        title = title,
        onTitleChange = { title = it },
        description = description,
        onDescriptionChange = { description = it },
        availableOpenings = availableOpenings,
        selectedOpenings = selectedOpenings,
        onAddOpening = { opening -> selectedOpenings = selectedOpenings + opening },
        onRemoveOpening = { opening -> selectedOpenings = selectedOpenings - opening },
        onSave = {
            val newGroup = Group(
                createdBy = (authState as? AuthenticationState.Authenticated)?.userId ?: "",
                title = title,
                description = description,
                openings = selectedOpenings
            )

            onCreateGroup(newGroup)
            navControllerPopBackStack()
        },
        onCancel = navControllerPopBackStack,
        saveButtonText = stringResource(R.string.groups_editor_create_button)
    )
}
