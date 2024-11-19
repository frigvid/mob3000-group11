package no.usn.mob3000.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.auth.error.AuthError
import no.usn.mob3000.domain.model.auth.state.ForgotPasswordState
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.base.Viewport

/**
 * This is the password reset request screen.
 *
 * @param onForgotPasswordClick Callback triggered when the user presses the "forgot password"
 *                              button to initiate the password reset process.
 * @author Anarox1111
 * @Contributor Markus, frigvid
 * @created 2024-09-24
 */
@Composable
fun ForgotPasswordScreen(
    onForgotPasswordClick: (String) -> Unit,
    forgotPasswordStateUpdate: (ForgotPasswordState) -> Unit,
    navControllerPopBackStack: () -> Unit
) {
    var showForgotPasswordConfirmation by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }

    if (showForgotPasswordConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.auth_forgot_password_confirmation),
            onConfirm = {
                showForgotPasswordConfirmation = false

                try {
                    onForgotPasswordClick(email)
                } catch (error: Exception) {
                    Logger.e("Something went wrong!", error)

                    forgotPasswordStateUpdate(
                        ForgotPasswordState.Error(AuthError.UserNotFound)
                    )
                }

                navControllerPopBackStack()
            },
            onDismiss = { showForgotPasswordConfirmation = false }
        )
    }

    Viewport { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = stringResource(R.string.auth_forgot_password_preamble))

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(stringResource(R.string.auth_login_email)) },
                    label = { Text(stringResource(R.string.auth_login_email_placeholder)) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showForgotPasswordConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.auth_forgot_password_reset))
                }
            }
        }
    }
}
