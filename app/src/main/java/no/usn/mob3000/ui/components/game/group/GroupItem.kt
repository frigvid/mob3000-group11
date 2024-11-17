package no.usn.mob3000.ui.components.game.group

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.game.Group
import no.usn.mob3000.domain.model.game.Opening
import no.usn.mob3000.ui.theme.DefaultButton
import no.usn.mob3000.ui.theme.DefaultListItemBackground

/**
 * Creates a list item for the scrollable list from any given group given.
 *
 * @param group The repertoire/group.
 * @param onOpeningClick Navigate to the opening's details.
 * @param onOpeningLongClick Remove the opening from the repertoire/group.
 * @param onEditClick Edit the repertoire/group.
 * @param onDeleteClick Delete the repertoire/group.
 * @param onTrainClick Select opening and go to chessboard to play.
 * @author frigvid
 * @created 2024-10-09
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupItem(
    group: Group,
    onOpeningClick: (Opening) -> Unit,
    onOpeningLongClick: (Opening, Group) -> Unit,
    onEditClick: (Group) -> Unit,
    onDeleteClick: (Group) -> Unit,
    onTrainClick: (Group) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group.title ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { expanded = !expanded }
                )

                Row {
                    IconButton(onClick = { onEditClick(group) }) {
                        Icon(
                            Icons.Default.Edit,
                            stringResource(R.string.groups_group_edit_button)
                        )
                    }
                    IconButton(onClick = { onDeleteClick(group) }) {
                        Icon(
                            Icons.Default.Delete,
                            stringResource(R.string.groups_group_delete_button),
                            tint = Color.Red
                        )
                    }
                }
            }

            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(12.dp),
                    color = Color.Black
                )

                Text(
                    text = stringResource(R.string.group_create_prompt_description),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = group.description ?: "",
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = { onTrainClick(group) },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.groups_group_train_all_button))
                }

                HorizontalDivider(
                    modifier = Modifier.padding(12.dp),
                    color = Color.Black
                )

                Text(
                    text = stringResource(R.string.group_create_selected_openings),
                    style = MaterialTheme.typography.titleMedium
                )

                group.openings.forEach { opening ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .combinedClickable(
                                onClick = { onOpeningClick(opening) },
                                onLongClick = { onOpeningLongClick(opening, group) }
                            ),
                        colors = CardDefaults.cardColors(containerColor = DefaultButton.copy(alpha = 0.7f))
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = opening.title ?: "",
                                modifier = Modifier.padding(12.dp)
                            )

                            Row {
                                IconButton(onClick = { onOpeningLongClick(opening, group) }) {
                                    Icon(
                                        Icons.Default.Close,
                                        stringResource(R.string.groups_group_delete_button)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
