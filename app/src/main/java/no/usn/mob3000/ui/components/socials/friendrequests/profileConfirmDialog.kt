package no.usn.mob3000.ui.components.socials.friendrequests

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

/**
 * Composable function that displays a confirm dialog
 *
 * this component lets the user create a confirm dialog for diffrent pages and scenarios to ask the users if they want to proceed or stop
 *
 * @param showDialog is a boolean value to check if the dialog is visible or not
 * @param onConfirm is a function that will run if the user wants to proceed with an action
 * @param onDismiss is a function that would hide the popup if the user wants to cancel or stop an action
 * @param title is a parameter to store the title of the dialog box
 * @param text is a parameter to store text about the dialog box
 * @param confirmtext is a parameter to store the text shown on the button used to accept an action
 * @param dismissText is a parameter to store the text shown on the button used to decline or stop an action
 * @author Husseinabdulameer11
 * @created 2024-11-16
 */
@Composable
fun profileConfirmDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = { showDialog.value = false },
    title: String,
    text: String,
    confirmtext: String,
    dismissText: String
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    showDialog.value = false
                }) {
                    Text(confirmtext)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(dismissText)
                }
            }
        )
    }
}
