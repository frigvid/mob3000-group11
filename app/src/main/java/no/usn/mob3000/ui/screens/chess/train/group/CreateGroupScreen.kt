package no.usn.mob3000.ui.screens.chess.train.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import no.usn.mob3000.ui.theme.DefaultListItemBackground
import no.usn.mob3000.ui.screens.chess.train.opening.Opening
import no.usn.mob3000.ui.screens.chess.train.opening.OpeningsScreen

/**
 * Screen to allow users to create opening groups/repertoires.
 *
 * This screen allows users to input a title and description for their group,
 * select from available openings, and add them to the group. It provides buttons
 * for saving the group and clearing the inputs.
 *
 * NOTE: It currently relies on the [OpeningsScreen] getting the available openings, since this
 *       gets the list from the ViewModel. This means that currently, if a user leaves the
 *       [OpeningsScreen] too quickly, it won't get the list. This is non-obvious and should be
 *       fixed.
 *
 * @param availableOpenings List of all available openings to choose from
 * @param onSaveGroup TODO
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun CreateGroupScreen(
    availableOpenings: List<Opening>,
    // TODO: onSaveGroup: (String, String, List<Opening>) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedOpenings by remember { mutableStateOf(listOf<Opening>()) }

    Viewport { innerPadding ->
        Scaffold(
            modifier = Modifier.padding(innerPadding),
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                title = ""
                                description = ""
                                selectedOpenings = emptyList()
                                /* TODO: onClearGroup() */
                            },
                            colors = ButtonDefaults.buttonColors(DefaultButton),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.group_create_reset_all))
                        }
                        Button(
                            onClick = { /* TODO: onSaveGroup(title, description, selectedOpenings) */ },
                            colors = ButtonDefaults.buttonColors(DefaultButton),
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        ) {
                            Text(stringResource(R.string.group_create_save_group))
                        }
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.group_create_prompt_title)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.group_create_prompt_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Text(
                    text = stringResource(R.string.group_create_available_openings),
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableOpenings.filter { it !in selectedOpenings }) { opening ->
                        OpeningCard(
                            opening = opening,
                            onClick = { selectedOpenings = selectedOpenings + opening },
                            icon = Icons.Default.Add,
                            iconContentDescription = "Add opening to group"
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.group_create_selected_openings),
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedOpenings) { opening ->
                        OpeningCard(
                            opening = opening,
                            onClick = { selectedOpenings = selectedOpenings - opening },
                            icon = Icons.Default.Close,
                            iconContentDescription = "Remove opening from group"
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable function that creates a card representing a chess opening.
 *
 * TODO: This should probably display at least some more details about the opening aside from the
 *       title. But it's a UX problem, so it can be fixed later.
 *
 * @param opening The [Opening] object to be displayed in the card.
 * @param onClick A lambda function that will be called when the card is clicked.
 * @param icon The [ImageVector] to be displayed as an icon on the right side of the card.
 * @param iconContentDescription A string describing the icon for accessibility purposes.
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun OpeningCard(
    opening: Opening,
    onClick: () -> Unit,
    icon: ImageVector,
    iconContentDescription: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
                           .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
                               .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(opening.title)

            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription
            )
        }
    }
}
