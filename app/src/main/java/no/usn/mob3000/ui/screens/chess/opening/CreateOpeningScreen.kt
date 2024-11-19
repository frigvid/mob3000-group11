package no.usn.mob3000.ui.screens.chess.opening

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.game.OpeningEditor

/**
 * Screen to allow users to create openings.
 *
 * This screen allows users to input a title and description for their opening, view a chessboard
 * representation of the moves (currently a placeholder), and interact with the opening creation
 * process through various buttons.
 *
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update authentication state.
 * @param openingsStartPeriodicUpdates Callback function to start periodic updates.
 * @param onSaveOpeningClick Callback function for when the save is confirmed.
 * @param popNavigationBackStack Callback function to navigate back a step.
 * @author frigvid
 * @created 2024-10-08
 */
@Composable
fun CreateOpeningScreen(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    openingsStartPeriodicUpdates: () -> Unit,
    onSaveOpeningClick: (Opening) -> Unit,
    popNavigationBackStack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val authState by remember { authenticationState }.collectAsState()
    LaunchedEffect(Unit) { authenticationStateUpdate() }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(stringResource(R.string.opening_create_auth_required_button))
                }
            },
            text = { Text(stringResource(R.string.opening_create_auth_required_message)) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }

    OpeningEditor(
        authState = authState,
        opening = null,
        onSave = { opening ->
            when (authState) {
                is AuthenticationState.Authenticated -> {
                    try {
                        onSaveOpeningClick(opening)
                        openingsStartPeriodicUpdates()
                        popNavigationBackStack()
                    } catch (error: Exception) {
                        Logger.e("Error saving opening", error)
                    }
                }

                is AuthenticationState.Unauthenticated -> showDialog = true

                else -> return@OpeningEditor
            }
        },
        onCancel = popNavigationBackStack,
        saveButtonText = stringResource(R.string.opening_create_save_opening)
    )
}
