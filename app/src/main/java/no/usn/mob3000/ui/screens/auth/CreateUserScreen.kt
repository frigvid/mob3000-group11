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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.flow.Flow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.error.RegistrationError
import no.usn.mob3000.domain.model.auth.state.RegistrationState
import no.usn.mob3000.ui.components.Loading
import no.usn.mob3000.ui.components.auth.Error
import no.usn.mob3000.ui.components.base.Viewport

/**
 * This is account registration screen.
 *
 * @param onSignUpClick Function to handle signing up a new account.
 * @param onReturnToLoginClick Callback function to navigate to [LoginScreen].
 * @author Markus, frigvid, Anarox1111
 * @created 2024-09-24
 */
@Composable
fun CreateUserScreen(
    registrationState: Flow<RegistrationState>,
    registrationStateUpdate: (RegistrationState) -> Unit,
    registrationStateReset: () -> Unit,
    onSignUpClick: (String, String) -> Unit,
    onReturnToLoginClick: () -> Unit
) {
    val state by registrationState.collectAsState(initial = RegistrationState.Idle)

    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                    value = mail,
                    onValueChange = { mail = it },
                    label = { Text(stringResource(R.string.auth_login_email)) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.auth_create_user_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.auth_create_user_confirm)) },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            onSignUpClick(mail, password)
                        } else {
                            registrationStateUpdate(
                                RegistrationState.Error(RegistrationError.PasswordsMustMatch)
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.auth_create_user_sign_up)) }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onReturnToLoginClick
                ) {
                    Text(
                        text = stringResource(R.string.auth_create_user_return_to_login),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                when (state) {
                    is RegistrationState.Success -> {
                        /* NOTE: State must be reset here, otherwise it will attempt
                         *       to navigate to the new destination multiple times.
                         */
                        registrationStateReset()

                        // TODO: Create a green "error" dialogue on the login screen
                        //       informing the user that they can log in now.
                        onReturnToLoginClick()
                    }

                    is RegistrationState.Error -> {
                        Error(text =
                            stringResource((state as RegistrationState.Error).error.messageRes)
                                + "\n"
                                + (state as RegistrationState.Error).error
                        )
                    }

                    is RegistrationState.Loading -> Loading()

                    else -> {  }
                }
            }
        }
    }
}
