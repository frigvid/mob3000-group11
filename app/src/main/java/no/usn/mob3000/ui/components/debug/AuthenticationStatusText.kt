package no.usn.mob3000.ui.components.debug

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.BuildConfig
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin

/**
 * Composable function to return login text only if build config
 * corresponds to debug and not build versions.
 *
 * @author frigvid
 * @created 2024-11-11
 */
@Composable
fun AuthenticationStatusText(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit
) {
    if (BuildConfig.DEBUG) {
        /**
         * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
         * additional details.
         *
         * @author frigvid
         * @created 2024-11-11
         */
        val state by remember { authenticationState }.collectAsState()
        LaunchedEffect(Unit) { authenticationStateUpdate() }

        val authenticationStatusText = when (state) {
            is AuthenticationState.Error,
            is AuthenticationState.Unauthenticated -> "Unauthenticated"
            is AuthenticationState.Authenticated -> "Authenticated"
            is AuthenticationState.Loading -> "Checking authentication..."
        }

        return Text(
            text = authenticationStatusText,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}
