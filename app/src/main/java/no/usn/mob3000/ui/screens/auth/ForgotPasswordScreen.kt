package no.usn.mob3000.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.usn.mob3000.ui.components.base.Viewport
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.error.AccountModificationError
import no.usn.mob3000.domain.model.auth.error.AuthError
import no.usn.mob3000.domain.model.auth.state.ChangeEmailState
import no.usn.mob3000.domain.model.auth.state.ForgotPasswordState
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.theme.DefaultButton

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
            title = stringResource(R.string.auth_email_confirmation),
            onConfirm = {
                showForgotPasswordConfirmation = false

                try {
                    onForgotPasswordClick(email)
                } catch (error: Exception) {
                    Log.e("ForgotPasswordScreen", "Something went wrong!", error)

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
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showForgotPasswordConfirmation = true },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.auth_forgot_password_reset))
                }
            }
        }
    }
}
