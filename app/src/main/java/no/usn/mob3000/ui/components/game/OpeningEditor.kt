package no.usn.mob3000.ui.components.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.game.board.ChessBoard
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * The chess opening creation and updating editor.
 *
 * @param opening Optional opening data to pre-fill the editor with.
 * @param authState Current authentication state to get user ID for new openings
 * @param onSave Callback for when the save button is clicked.
 * @param onCancel Callback for when the cancel button is clicked.
 * @param saveButtonText Text to display on the save button.
 * @author frigvid
 * @created 2024-11-14
 */
@Composable
fun OpeningEditor(
    authState: AuthenticationState?,
    opening: Opening?,
    onSave: (Opening) -> Unit,
    onCancel: () -> Unit,
    saveButtonText: String
) {
    var title by remember { mutableStateOf(opening?.title ?: "") }
    var description by remember { mutableStateOf(opening?.description ?: "") }
    val moves by remember { mutableStateOf(opening?.moves ?: "") }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.opening_editor_prompt_title)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.opening_editor_prompt_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            /* TODO: Go back-forward in history. */
            ChessBoard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: Implement undo move logic */ },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) { Text(stringResource(R.string.opening_editor_undo_move)) }

                Button(
                    onClick = { /* TODO: Implement reset board logic */ },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) { Text(stringResource(R.string.opening_editor_reset_board)) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) { Text(stringResource(R.string.opening_editor_reset_all)) }

                Button(
                    onClick = {
                        val newOpening =
                            if (opening != null) {
                                opening.copy(
                                    title = title,
                                    description = description,
                                    moves = moves
                                )
                            } else {
                                Opening(
                                    title = title,
                                    description = description,
                                    moves = moves,
                                    createdBy = (authState as? AuthenticationState.Authenticated)?.userId
                                )
                            }
                        onSave(newOpening)
                    },
                    enabled = title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(saveButtonText)
                }
            }
        }
    }
}
