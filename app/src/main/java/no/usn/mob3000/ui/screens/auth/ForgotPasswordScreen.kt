package no.usn.mob3000.ui.screens.auth

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
import no.usn.mob3000.Viewport
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * This is the chessboard page, where users can free-play against an AI or
 * fellow physically near player. (Local multiplayer, in other words).
 *
 * @param onResetPassword Callback triggered when the user presses the "Reset
 * password" button to initiate the password reset process.
 *
 * @see LoginScreen
 * @see ResetPasswordScreen
 * @author Anarox, frigvid
 * @Contributor Markus
 * @created 2024-09-24
 */
@Composable
fun ForgotPasswordScreen(
    onResetPassword: () -> Unit
) {
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
                Text(
                    text = "Type in your email that is linked to your account. You will receive a mail with a link to reset your password.",
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                var email by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Your email") },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onResetPassword() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reset password")
                }
            }
        }
    }
}