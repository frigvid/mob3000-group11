package no.usn.mob3000.ui.screens.chess.opening

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
import androidx.compose.material3.MaterialTheme
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
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.helper.game.convertPgnToFen
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.game.board.ChessBoard
import no.usn.mob3000.ui.screens.chess.PlayScreen

/**
 * Screen that shows the details for any given opening that has been navigated to from
 * the [OpeningsScreen].
 *
 * TODO: Add functionality to traverse steps in the opening, akin to website version.
 * TODO: Extract data/logic related code to data layer.
 *
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update the authentication status state.
 * @param opening The [Opening] to display details about.
 * @param navigateToPlayScreen Callback function to navigate to the [PlayScreen].
 * @param onDeleteOpeningClick Callback function to delete an opening.
 * @param setSelectedOpening Track an opening in state.
 * @param setSelectedBoardOpenings Track a list of openings in state.
 * @param navigateToOpeningEditor Callback function to navigate to the opening editor.
 * @param popNavigationBackStack Callback function to pop the navigation controller's back stack.
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
                                Logger.d("Deleting opening with ID: ${opening.id}")
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

                    /* TODO: Go back-forward in history. */
                    ChessBoard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        startingPosition = convertPgnToFen(listOf(opening).first().moves ?: ""),
                        gameInteractable = false
                    )

                    Text(
                        text = "FEN: ${convertPgnToFen(listOf(opening).first().moves ?: "")}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            setSelectedBoardOpenings(listOf(opening))
                            navigateToPlayScreen()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
