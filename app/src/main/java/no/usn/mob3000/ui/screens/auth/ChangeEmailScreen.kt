package no.usn.mob3000.ui.screens.auth

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.error.AccountModificationError
import no.usn.mob3000.domain.model.auth.state.ChangeEmailState
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.Loading
import no.usn.mob3000.ui.components.auth.Error
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This is the e-mail address change screen, where users can change their e-mails.
 *
 * @author frigvid
 * @created 2024-11-13
 */
@Composable
fun ChangeEmailScreen(
    changeEmailStateUpdate: (ChangeEmailState) -> Unit,
    onChangeEmailClick: (String) -> Unit,
    navControllerPopBackStack: () -> Unit,
    authenticationStateUpdate: () -> Unit
) {
    var showEmailChangeConfirmation by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }

    if (showEmailChangeConfirmation) {
        DangerousActionDialogue(
            title = stringResource(R.string.auth_email_confirmation),
            onConfirm = {
                showEmailChangeConfirmation = false

                if (email == confirmEmail) {
                    onChangeEmailClick(email)
                } else {
                    changeEmailStateUpdate(
                        ChangeEmailState.Error(AccountModificationError.EmailMustMatch)
                    )
                }

                authenticationStateUpdate()
                navControllerPopBackStack()
            },
            onDismiss = { showEmailChangeConfirmation = false }
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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.auth_email_input)) },
                    placeholder = { Text(stringResource(R.string.auth_login_email_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = confirmEmail,
                    onValueChange = { confirmEmail = it },
                    label = { Text(stringResource(R.string.auth_email_input_confirm)) },
                    placeholder = { Text(stringResource(R.string.auth_login_email_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // TODO: If fields are empty, change text and action. Should
                //       take user back to settings. If there are text, change
                //       it to say and do what it does now.
                Button(
                    onClick = { showEmailChangeConfirmation = true },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.auth_email_button))
                }
            }
        }
    }
}
