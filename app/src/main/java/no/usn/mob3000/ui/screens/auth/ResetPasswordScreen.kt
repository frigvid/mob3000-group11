package no.usn.mob3000.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import no.usn.mob3000.Viewport

/**
 * This is the chessboard page, where users can free-play against an AI or
 * fellow physically near player. (Local multiplayer, in other words).
 *
 * @see LoginScreen
 * @see ForgotPasswordScreen
 * @author Markus
 * @created 2024-09-24
 */
@Composable
fun ResetPasswordScreen(
    onReset: () -> Unit
) {

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
                Text(stringResource(R.string.auth_resetPassword_title))

                Spacer(modifier = Modifier.height(16.dp))

                // Password input
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("New password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm password
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm new password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Reset Button
                Button(
                    onClick = { onReset() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Password")
                }
            }
        }
    }
}