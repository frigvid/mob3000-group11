package no.usn.mob3000.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

/**
 * This is the chessboard page, where users can free-play against an AI or
 * fellow physically near player. (Local multiplayer, in other words).
 *
 * @see ForgotPasswordScreen
 * @see ResetPasswordScreen
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun LoginScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.auth_login_title))
    }
}