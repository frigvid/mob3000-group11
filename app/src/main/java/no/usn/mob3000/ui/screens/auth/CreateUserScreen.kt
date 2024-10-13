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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 * This is account registration screen.
 *
 * @param onSignUpClick Function to handle signing up a new account.
 * @param onReturnToLoginClick Callback function to navigate to [LoginScreen].
 * @author Markus
 * @contributor frigvid
 * @created 2024-09-24
 */
@Composable
fun CreateUserScreen(
    onSignUpClick: () -> Unit,
    onReturnToLoginClick: () -> Unit
) {
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
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.auth_create_user_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.auth_create_user_confirm)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSignUpClick() },
                    /* TODO: Replace with DefaultButton theme color when up-to-date with master. */
                    colors = ButtonDefaults.buttonColors(Color(0XFFC0A080)),
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.auth_create_user_sign_up)) }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onReturnToLoginClick
                ) { Text(stringResource(R.string.auth_create_user_return_to_login)) }
            }
        }
    }
}