package no.usn.mob3000.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.usn.mob3000.R

/**
 * A reusable confirmation dialog component with dangerous action highlighting.
 *
 * This dialog is designed for confirming dangerous/destructive actions, with the confirmation
 * button highlighted in red to indicate the severity of the action.
 *
 * @param title The title text to display in the dialog
 * @param onConfirm Callback function when the user confirms the action
 * @param onDismiss Callback function when the dialog is dismissed
 * @param confirmText Text for the confirm button (defaults to "Yes")
 * @param dismissText Text for the dismiss button (defaults to "No")
 * @author frigvid
 * @created 2024-11-03
 */
@Composable
fun DangerousActionDialogue(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = stringResource(R.string.component_dialogue_dangerous_action_button_confirm),
    dismissText: String = stringResource(R.string.component_dialogue_dangerous_action_button_dismiss),
    colorContainer: Color = MaterialTheme.colorScheme.surface,
    colorButtonDismiss: Color = MaterialTheme.colorScheme.primary,
    colorButtonConfirm: Color = MaterialTheme.colorScheme.error
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(colorButtonDismiss),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) { Text(dismissText) }

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(colorButtonConfirm),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) { Text(confirmText) }
                }
            }
        }
    }
}
