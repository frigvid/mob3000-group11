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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import no.usn.mob3000.R
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This screen allows users to log into accounts, request new passwords ([ForgotPasswordScreen]),
 * and register new accounts ([CreateUserScreen]).
 *
 * TODO: Make use of ViewModel to store stateful data like user account details.
 *
 * @param onLoginClick Callback triggered when the user presses the "Log In" button
 *                     to authenticate their account.
 * @param onForgotPasswordClick Callback triggered when the user clicks the "Forgot password?"
 *                              text to navigate to the password recovery screen.
 * @param onCreateUserClick Callback triggered when the user clicks the "Or sign in"
 *                          text to navigate to the user creation screen.
 * @author frigvid
 * @contributor Anarox, Markus
 * @created 2024-09-30
 */
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateUserClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.auth_login_email)) },
                    placeholder = { Text(stringResource(R.string.auth_login_email_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.auth_login_password)) },
                    placeholder = { Text(stringResource(R.string.auth_login_password_placeholder)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Button(
                    onClick = onLoginClick,
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
            }
        }
    }
}