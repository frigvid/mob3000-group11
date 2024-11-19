package no.usn.mob3000.ui.screens.chess.opening

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.game.OpeningEditor

/**
 * Screen for updating existing chess openings.
 *
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update authentication state.
 * @param opening The opening to edit.
 * @param onUpdateOpeningClick Callback function for when the update is confirmed.
 * @param openingsStartPeriodicUpdates Callback function to start periodic updates.
 * @param popNavigationBackStack Callback function to navigate back.
 * @author frigvid
 * @created 2024-11-14
 */
@Composable
fun UpdateOpeningScreen(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    opening: Opening?,
    onUpdateOpeningClick: (Opening) -> Unit,
    openingsStartPeriodicUpdates: () -> Unit,
    popNavigationBackStack: () -> Unit
) {
    val authState by remember { authenticationState }.collectAsState()
    LaunchedEffect(Unit) { authenticationStateUpdate() }

    if (opening != null) {
        OpeningEditor(
            authState = authState,
            opening = opening,
            onSave = { updatedOpening ->
                try {
                    onUpdateOpeningClick(updatedOpening)
                    openingsStartPeriodicUpdates()
                    popNavigationBackStack()
                } catch (error: Exception) {
                    Logger.e("Error updating opening", error)
                }
            },
            onCancel = popNavigationBackStack,
            saveButtonText = stringResource(R.string.opening_update_save_opening)
        )
    }
}
