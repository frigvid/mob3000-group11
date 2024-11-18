package no.usn.mob3000.ui.components.settings

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.AuthenticationState

/**
 * Composable component for the settings screen.
 *
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update the authentication status state immediately.
 * @param onAdminDashboardClick Callback function to navigate to the administrator dashboard.
 * @author frigvid
 * @created 2024-11-11
 */
@Composable
fun SettingsSectionAdmin(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    onAdminDashboardClick: () -> Unit
) {
    /**
     * You'd think just a simple view model `authState.collectAsState()` or
     * `authState.collectAsStateWithLifecycle()` would work, but no. It's harder
     * than necessary to access companion objects in the authentication state that
     * way. Thus, this atrocity of mankind if born. Truly, a crime against humanity,
     * if there ever was one.
     *
     * Honestly though, it's not *that* bad. And it works well enough.
     *
     * @author frigvid
     * @created 2024-11-11
     */
    val state by remember { authenticationState }.collectAsState()
    LaunchedEffect(Unit) { authenticationStateUpdate() }

    when (val authState = state) {
        is AuthenticationState.Authenticated -> {
            if (authState.isAdmin) {
                Text(
                    "[ " + stringResource(R.string.settings_section_admin_subtitle) + " ]",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Button(
                    onClick = onAdminDashboardClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) { Text(stringResource(R.string.settings_section_admin_button_admin)) }
            }
        }
        else -> Log.d("SettingsSectionAdmin", "User is not an administrator. Not showing admin section!")
    }
}
