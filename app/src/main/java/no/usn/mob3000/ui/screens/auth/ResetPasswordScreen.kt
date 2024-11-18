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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.error.AccountModificationError
import no.usn.mob3000.domain.model.auth.state.ChangePasswordState
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This is the password reset screen, where users can change their passwords.
 *
 * @param onResetPasswordClick Callback triggered when the user presses the "Reset Password"
 *                             button to initiate the password reset process.
 * @author Markus, frigvid
 * @created 2024-09-24
 */
@Composable
fun ResetPasswordScreen(
    onResetPasswordClick: (String) -> Unit,
    changePasswordStateUpdate: (ChangePasswordState) -> Unit,
    authenticationStateUpdate: () -> Unit,
    navControllerPopBackStack: () -> Unit
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
                    onResetPasswordClick(password)
                } else {
                    changePasswordStateUpdate(
                        ChangePasswordState.Error(AccountModificationError.PasswordMustMatch)
                    )
                }

                authenticationStateUpdate()
                navControllerPopBackStack()
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.auth_reset_password_button))
                }
            }
        }
    }
}
