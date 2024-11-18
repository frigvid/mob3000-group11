package no.usn.mob3000.ui.components.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.error.AccountModificationError
import no.usn.mob3000.domain.model.auth.state.ChangePasswordState
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.base.Viewport
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.TextStyle

/**
 * Shared component for password reset functionality that can be used across different screens.
 *
 * @param onPasswordReset Callback triggered when password reset is confirmed. Takes the new password as parameter.
 * @param onChangePasswordStateUpdate Callback to update the password change state.
 * @param onAuthenticationStateUpdate Callback to update the authentication state.
 * @param onNavigateBack Callback to handle navigation after password reset.
 * @author frigvid, Anarox
 * @created 2024-11-18
 */
@Composable
fun PasswordResetContent(
    onPasswordReset: (String) -> Unit,
    onChangePasswordStateUpdate: (ChangePasswordState) -> Unit,
    onAuthenticationStateUpdate: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showPasswordChangeConfirmation by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    if (showPasswordChangeConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.auth_reset_password_confirmation),
            onConfirm = {
                showPasswordChangeConfirmation = false

                if (password == confirmPassword) {
                    onPasswordReset(password)
                } else {
                    onChangePasswordStateUpdate(
                        ChangePasswordState.Error(AccountModificationError.PasswordMustMatch)
                    )
                }

                onAuthenticationStateUpdate()
                onNavigateBack()
            },
            onDismiss = { showPasswordChangeConfirmation = false }
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
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.auth_reset_password_input)) },
                    placeholder = { Text(stringResource(R.string.auth_login_password_placeholder)) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.auth_reset_password_confirm)) },
                    placeholder = { Text(stringResource(R.string.auth_login_password_placeholder)) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showPasswordChangeConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor =  MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.auth_reset_password_button))
                }
            }
        }
    }
}
