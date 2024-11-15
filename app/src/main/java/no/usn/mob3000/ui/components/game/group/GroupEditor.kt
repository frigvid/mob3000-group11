package no.usn.mob3000.ui.components.game.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.usn.mob3000.domain.model.game.Opening
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * The editor for repertoires/groups.
 *
 * @param title The title of the repertoire/group.
 * @param onTitleChange State tracking changes to the title.
 * @param description The description of the repertoire/group.
 * @param onDescriptionChange State tracking changes to the description.
 * @param availableOpenings A list of the available openings.
 * @param selectedOpenings A list of the selected openings.
 * @param onAddOpening Arbitrary unit meant to be used to add the opening to the selected openings.
 * @param onRemoveOpening Arbitrary unit meant to be used to remove the opening from the selected openings.
 * @param onSave Arbitrary unit meant to handle saving/updating the repertoire/group.
 * @param onCancel Navigation controller pop back stack.
 * @param saveButtonText The text of the save button.
 * @author frigvid
 * @created 2024-11-15
 */
@Composable
fun GroupEditor(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    availableOpenings: List<Opening>,
    selectedOpenings: List<Opening>,
    onAddOpening: (Opening) -> Unit,
    onRemoveOpening: (Opening) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    saveButtonText: String
) {
    Viewport(
        topBarActions = {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onSave,
                    enabled = title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(saveButtonText)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Group title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Group description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Text(
                text = "Available openings",
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
                        onClick = { onAddOpening(opening) },
                        icon = Icons.Default.Add,
                        iconContentDescription = "Add opening to group"
                    )
                }
            }

            Text(
                text = "Selected openings",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedOpenings) { opening ->
                    OpeningCard(
                        opening = opening,
                        onClick = { onRemoveOpening(opening) },
                        icon = Icons.Default.Close,
                        iconContentDescription = "Remove opening from group"
                    )
                }
            }
        }
    }
}
