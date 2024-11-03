import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

/**
 * Confirmation dialog for deleting items, used for several screens
 *
 * @author 258030
 * @created 2024-10-30
 */
@Composable
fun ConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = { showDialog.value = false }
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.confirmation_dialog_confirm)) },
            text = { Text(stringResource(R.string.confirmation_dialog_delete_text)) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    showDialog.value = false
                }) {
                    Text(stringResource(R.string.confirmation_dialog_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.confirmation_dialog_cancel))
                }
            }
        )
    }
}
