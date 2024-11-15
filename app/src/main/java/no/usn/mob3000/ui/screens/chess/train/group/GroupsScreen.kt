package no.usn.mob3000.ui.screens.chess.train.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.usn.mob3000.domain.model.game.Group
import no.usn.mob3000.domain.model.game.Opening
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.game.group.GroupItem
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This shows the various chess opening repertoires/groups that have been created by the active user.
 *
 * @param groupsList The list of repertoires/groups.
 * @param openingsStartPeriodicUpdates Callback function to schedule an update for openings.
 * @param groupsStartPeriodicUpdates Callback function to schedule an update for repertoires/groups
 * @param onOpeningSelect Store the selected opening in its corresponding ViewModel.
 * @param setSelectedGroup Store the selected repertoire/group in its corresponding ViewModel.
 * @param onGroupDelete Delete the opening matching the string ID.
 * @param onNavigateToGroupCreation Callback function to navigate to the repertoire/group creation screen.
 * @param onNavigateToGroupEditing Callback function to navigate to the repertoire/group editing screen.
 * @param onNavigateToOpeningDetails Callback function to navigate to the opening details screen.
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun GroupsScreen(
    groupsList: List<Group>,
    openingsStartPeriodicUpdates: () -> Unit,
    groupsStartPeriodicUpdates: () -> Unit,
    onOpeningSelect: (Opening) -> Unit,
    setSelectedGroup: (Group) -> Unit,
    onGroupDelete: (String) -> Unit,
    onNavigateToGroupCreation: () -> Unit,
    onNavigateToGroupEditing: () -> Unit,
    onNavigateToOpeningDetails: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf<Group?>(null) }
    var showRemoveOpeningConfirm by remember { mutableStateOf<Pair<Opening, Group>?>(null) }

    LaunchedEffect(showRemoveOpeningConfirm) { groupsStartPeriodicUpdates() }
    LaunchedEffect(showDeleteConfirm) {
        openingsStartPeriodicUpdates()
        groupsStartPeriodicUpdates()
    }

    if (showDeleteConfirm != null) {
        DangerousActionDialogue(
            title = "Delete this group?",
            onConfirm = {
                onGroupDelete(showDeleteConfirm!!.id)
                showDeleteConfirm = null
            },
            onDismiss = { showDeleteConfirm = null }
        )
    }

    if (showRemoveOpeningConfirm != null) {
        val (opening, group) = showRemoveOpeningConfirm!!
        DangerousActionDialogue(
            title = "Remove this opening from the group?",
            onConfirm = {
                setSelectedGroup(group.copy(openings = group.openings - opening))
                showRemoveOpeningConfirm = null
            },
            onDismiss = { showRemoveOpeningConfirm = null }
        )
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DefaultButton,
                onClick = onNavigateToGroupCreation
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Group")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(groupsList) { group ->
                GroupItem(
                    group = group,
                    onOpeningClick = { opening ->
                        onOpeningSelect(opening)
                        onNavigateToOpeningDetails()
                    },
                    onOpeningLongClick = { opening, _ ->
                        showRemoveOpeningConfirm = opening to group
                    },
                    onEditClick = {
                        setSelectedGroup(group)
                        onNavigateToGroupEditing()
                    },
                    onDeleteClick = { showDeleteConfirm = it },
                    onTrainClick = setSelectedGroup
                )
            }
        }
    }
}
