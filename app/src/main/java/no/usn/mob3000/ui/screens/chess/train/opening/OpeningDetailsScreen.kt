package no.usn.mob3000.ui.screens.chess.train.opening

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import no.usn.mob3000.ui.screens.chess.PlayScreen

/**
 * Screen that shows the details for any given opening that has been navigated to from
 * the [OpeningsScreen].
 *
 * TODO: Add functionality to traverse steps in the opening, akin to website version.
 * TODO: Extract data/logic related code to data layer.
 *
 * @param opening The [Opening] to display details about.
 * @param onPracticeClick Callback function to navigate to the [PlayScreen].
 * @author frigvid
 * @created 2024-10-08
 */
@Composable
fun OpeningDetailsScreen(
    opening: Opening?,
    onPracticeClick: () -> Unit
) {
    Viewport { innerPadding ->
        if (opening != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = opening.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.opening_details_description),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = opening.description,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.opening_details_moves),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                opening.pgn.forEachIndexed { index, move ->
                    /* TODO: Extract string resources. Should also probably not be designed this way. */
                    val pieceFullName = when (move["piece"]) {
                        "p" -> "pawn"
                        "q" -> "queen"
                        "k" -> "king"
                        "b" -> "bishop"
                        "n" -> "knight"
                        "r" -> "rook"
                        else -> "piece"
                    }

                    /* TODO: Extract as string resource. */
                    val moveDescription = "Move $pieceFullName from ${move["from"]?.uppercase()} to ${move["to"]?.uppercase()}"

                    Text(
                        text = "${index + 1}. $moveDescription",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                /* TODO: Stick this to the bottom of the screen, or something. Should probably be
                 *       floating.
                 */
                Button(
                    onClick = onPracticeClick,
                    colors = ButtonDefaults.buttonColors(DefaultButton),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(stringResource(R.string.opening_details_practice_button))
                }
            }
        } else {
            /* TODO: This is entirely temporary. Replace with something less ... terrible. */
            Text("Howdy neighbour, you seem to have forgotten your opening.")
        }
    }
}
