package no.usn.mob3000.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import no.usn.mob3000.Viewport
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.flow.Flow
import no.usn.mob3000.Destination
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.LoginState
import no.usn.mob3000.ui.components.Loading
import no.usn.mob3000.ui.components.auth.Error
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This screen allows users to log into accounts, request new passwords ([ForgotPasswordScreen]),
 * and register new accounts ([CreateUserScreen]).
 *
 * TODO: Make use of ViewModel to store stateful data like user account details.
 *
 * @param loginState The current login state from [LoginViewModel].
 * @param loginStateReset Callback function for properly handling state changes.
 * @param navigateHome Callback function to navigate to [Destination.HOME].
 * @param onLoginClick Callback triggered when the user presses the "Log In" button
 *                     to authenticate their account.
 * @param onForgotPasswordClick Callback triggered when the user clicks the "Forgot password?"
 *                              text to navigate to the password recovery screen.
 * @param onCreateUserClick Callback triggered when the user clicks the "Or sign in"
 *                          text to navigate to the user creation screen.
 * @author frigvid, Anarox1111
 * @contributor Markus
 * @created 2024-09-30
 */
@Composable
fun LoginScreen(
    loginState: Flow<LoginState>,
    loginStateReset: () -> Unit,
    navigateHome: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateUserClick: () -> Unit
) {
    val state by loginState.collectAsState(initial = LoginState.Idle)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }

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
                OutlinedTextField(
                    value = inputEmail,
                    onValueChange = { inputEmail = it },
                    label = { Text(stringResource(R.string.auth_login_email)) },
                    placeholder = { Text(stringResource(R.string.auth_login_email_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = inputPassword,
                    onValueChange = { inputPassword = it },
                    label = { Text(stringResource(R.string.auth_login_password)) },
                    placeholder = { Text(stringResource(R.string.auth_login_password_placeholder)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        loginStateReset()
                        onLoginClick(inputEmail, inputPassword)
                    },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) { Text(stringResource(R.string.auth_login_authenticate)) }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text(stringResource(R.string.auth_login_forgot_password)) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))

                    Text(
                        stringResource(R.string.auth_login_divider),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )

                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.auth_login_register_reminder))

                    Button(
                        onClick = onCreateUserClick,
                        colors = ButtonDefaults.buttonColors(DefaultButton)
                    ) { Text(stringResource(R.string.auth_login_register)) }
                }

                when (state) {
                    is LoginState.Success -> {
                        /* NOTE: State must be reset here, otherwise it will attempt
                         *       to navigate to HOME multiple times. For some
                         *       inexplicable reason.
                         */
                        loginStateReset()

                        navigateHome()
                    }

                    is LoginState.Error -> {
                        Error(text =
                            stringResource((state as LoginState.Error).error.messageRes)
                                + "\n"
                                + (state as LoginState.Error).error
                        )
                    }

                    is LoginState.Loading -> Loading()

                    else -> {  }
                }
            }
        }
    }
}
