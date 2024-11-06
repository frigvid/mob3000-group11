package no.usn.mob3000.ui.screens.chess.train.opening

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen to allow users to create openings.
 *
 * This screen allows users to input a title and description for their opening, view a chessboard
 * representation of the moves (currently a placeholder), and interact with the opening creation
 * process through various buttons.
 *
 * @param onSaveOpening A lambda function that will be called when the user attempts to save the
 *                      opening. It should take two String parameters: the title and description
 *                      of the opening.
 * @param onClearOpening A lambda function that will be called when the user wants to clear all
 *                       inputs.
 * @author frigvid
 * @created 2024-10-08
 */
@Composable
fun CreateOpeningScreen(
    // TODO: onSaveOpening: (String, String) -> Unit,
    // TODO: onClearOpening: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
                label = { Text(stringResource(R.string.opening_create_prompt_title)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.opening_create_prompt_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Text(
                text = stringResource(R.string.opening_create_moves),
                style = MaterialTheme.typography.titleMedium
            )

            Image(
                painter = painterResource(id = R.drawable.placeholder_chess),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: Implement undo move logic */ },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.opening_create_undo_move))
                }
                Button(
                    onClick = { /* TODO: Implement reset board logic */ },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.opening_create_reset_board))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    /* TODO: This should be extracted into its own function. Saving needs to also
                     *       clear old data.
                     */
                    onClick = {
                        title = ""
                        description = ""
                        /* TODO: onClearOpening() */
                    },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.opening_create_reset_all))
                }
                Button(
                    onClick = { /* TODO: onSaveOpening(title, description) */ },
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.opening_create_save_opening))
                }
            }
        }
    }
}
