package no.usn.mob3000.ui.screens.chess.train.group

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.game.group.Group
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.game.group.GroupEditor

/**
 * Screen for updating a repertoire/group.
 *
 * @param group The selected group.
 * @param availableOpenings The available openings.
 * @param onUpdateGroupClick Update the selected group with changes.
 * @param navControllerPopBackStack Pop the navigation controller's back stack.
 * @author frigvid
 * @created 2024-11-15
 */
@Composable
fun UpdateGroupScreen(
    group: Group?,
    availableOpenings: List<Opening>,
    onUpdateGroupClick: (Group) -> Unit,
    navControllerPopBackStack: () -> Unit
) {
    if (group == null) {
        navControllerPopBackStack()
        return
    }

    var title by remember { mutableStateOf(group.title ?: "") }
    var description by remember { mutableStateOf(group.description ?: "") }
    var selectedOpenings by remember { mutableStateOf(group.openings) }

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
            onUpdateGroupClick(
                group.copy(
                    title = title,
                    description = description,
                    openings = selectedOpenings
                )
            )

            navControllerPopBackStack()
        },
        onCancel = navControllerPopBackStack,
        saveButtonText = stringResource(R.string.groups_editor_update_button)
    )
}
