package no.usn.mob3000.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport


/**
 * This is the login screen for authorization to an account
 * as well as an option for logging in with a guest account.
 *
 * @see ForgotPasswordScreen
 * @see ResetPasswordScreen
 * @author Anarox
 * @created 2024-09-30
 */
@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onForgot: () -> Unit
) {
    var username by remember { mutableStateOf("") }
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
                // Title
                Text(text = stringResource(R.string.auth_login_title))

                Spacer(modifier = Modifier.height(16.dp))

                // Username input
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Brukernavn") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password input
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Passord") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logg in button
                Button(
                    onClick = { onLogin() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Gjestebruker knapp
                Button(
                    onClick = { onLogin() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log in as guest")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Forgot password?
                Text(
                    text = "Forgot password?",
                    color = Color(0xFF7F563B),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                ),
                    modifier = Modifier.clickable {
                        onForgot()
                    })
            }
        }
    }
}