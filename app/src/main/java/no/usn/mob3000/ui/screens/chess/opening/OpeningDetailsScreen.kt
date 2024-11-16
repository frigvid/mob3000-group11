package no.usn.mob3000.ui.screens.chess.opening

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.screens.chess.PlayScreen
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen that shows the details for any given opening that has been navigated to from
 * the [OpeningsScreen].
 *
 * TODO: Add functionality to traverse steps in the opening, akin to website version.
 * TODO: Extract data/logic related code to data layer.
 *
 * @param opening The [Opening] to display details about.
 * @param navigateToPlayScreen Callback function to navigate to the [PlayScreen].
 * @author frigvid
 * @created 2024-10-08
 */
@Composable
fun OpeningDetailsScreen(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    opening: Opening?,
    navigateToPlayScreen: () -> Unit,
    onDeleteOpeningClick: (String) -> Unit,
    setSelectedOpening: (Opening) -> Unit,
    setSelectedBoardOpenings: (List<Opening>) -> Unit,
    navigateToOpeningEditor: () -> Unit,
    popNavigationBackStack: () -> Unit
) {
    /**
     * See `SettingsSectionAdmin`'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    val authState by remember { authenticationState }.collectAsState()
    LaunchedEffect(Unit) { authenticationStateUpdate() }

    Viewport(
        topBarActions = {
            when (val auth = authState) {
                is AuthenticationState.Authenticated -> {
                    if (auth.isAdmin && opening != null) {
                        // TODO: DangerousActionDialog
                        IconButton(
                            onClick = {
                                Log.d("OpeningDetailsScreen", "Deleting opening with ID: ${opening.id}")
                                onDeleteOpeningClick(opening.id)
                                popNavigationBackStack()
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete opening",
                                tint = Color.Red
                            )
                        }

                        IconButton(
                            onClick = {
                                setSelectedOpening(opening)
                                navigateToOpeningEditor()
                            }
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit opening"
                            )
                        }
                    }
                }

                else -> return@Viewport
            }
        }
    ) { innerPadding ->
        if (opening != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = opening.title ?: "\uD83D\uDC4B\uD83D\uDE00",
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
                        text = opening.description ?: "\uD83D\uDC4B\uD83D\uDE00",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = stringResource(R.string.opening_details_moves),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // TODO: chess.
                    //opening.moves.forEachIndexed { index, move ->
                    //    /* TODO: Extract string resources. Should also probably not be designed this way. */
                    //    val pieceFullName = when (move["piece"]) {
                    //        "p" -> "pawn"
                    //        "q" -> "queen"
                    //        "k" -> "king"
                    //        "b" -> "bishop"
                    //        "n" -> "knight"
                    //        "r" -> "rook"
                    //        else -> "piece"
                    //    }
                    //
                    //    /* TODO: Extract as string resource. */
                    //    val moveDescription = "Move $pieceFullName from ${move["from"]?.uppercase()} to ${move["to"]?.uppercase()}"
                    //
                    //    Text(
                    //        text = "${index + 1}. $moveDescription",
                    //        modifier = Modifier
                    //            .fillMaxWidth()
                    //            .padding(bottom = 4.dp)
                    //    )
                    //}
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    /* TODO: Stick this to the bottom of the screen, or something. Should probably be
                     *       floating.
                     */
                    Button(
                        onClick = {
                            setSelectedBoardOpenings(listOf(opening))
                            navigateToPlayScreen()
                        },
                        colors = ButtonDefaults.buttonColors(DefaultButton),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(stringResource(R.string.opening_details_practice_button))
                    }
                }
            }
        } else {
            /* TODO: This is entirely temporary. Replace with something less ... terrible. */
            Text("Howdy neighbour, you seem to have forgotten your opening.")
        }
    }
}
