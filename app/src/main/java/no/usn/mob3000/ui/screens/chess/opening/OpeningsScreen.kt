package no.usn.mob3000.ui.screens.chess.opening

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.helper.game.convertPgnToFen
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.game.board.ChessBoard
import no.usn.mob3000.ui.components.settings.SettingsSectionAdmin
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This shows the various chess openings that are available by default, and that
 * are created by users.
 *
 * Its sibling screen, which is accessible from this one, is the `GroupsScreen`.
 *
 * @param authenticationState The authentication status state.
 * @param authenticationStateUpdate Callback function to update the authentication status state immediately.
 * @param openingsStartPeriodicUpdates Callback function to schedule a periodic update.
 * @param setSelectedOpening ViewModel function to set the selected opening.
 * @param openings The list of openings tracked in state.
 * @param onGroupsClick Callback function to navigate to the `GroupsScreen`.
 * @param onCreateOpeningClick Callback function to navigate to the [CreateOpeningScreen].
 * @param onOpeningClick Callback function to navigate to the [OpeningDetailsScreen].
 * @param filter TODO: Optional list of string IDs to filter openings
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun OpeningsScreen(
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    openingsStartPeriodicUpdates: () -> Unit,
    setSelectedOpening: (Opening) -> Unit,
    openings: List<Opening>,
    onGroupsClick: () -> Unit,
    onCreateOpeningClick: () -> Unit,
    onOpeningClick: (Opening) -> Unit,
    filter: List<String>? = null
) {
    val openingsList by remember { mutableStateOf(openings) }

    // TODO: Hide top bar and bottom bar when in landscape.
    //val configuration = LocalConfiguration.current
    //val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    /**
     * See [SettingsSectionAdmin]'s docstring for [authenticationState] for
     * additional details.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    val authState by remember { authenticationState }.collectAsState()

    LaunchedEffect(Unit) {
        authenticationStateUpdate()
        openingsStartPeriodicUpdates()
    }

    Viewport (
        floatingActionButton = {
            when (val auth = authState) {
                is AuthenticationState.Authenticated -> {
                    if (auth.isAdmin) {
                        FloatingActionButton(
                            containerColor = DefaultButton,
                            onClick = onCreateOpeningClick
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Create opening"
                            )
                        }
                    }
                }

                else -> return@Viewport
            }
        },
        topBarActions = {
            when (val auth = authState) {
                is AuthenticationState.Authenticated -> {
                    if (auth.isAdmin) {
                        IconButton(
                            onClick = onGroupsClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.group_button),
                                contentDescription = "Groups",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }
                    }
                }

                else -> return@Viewport
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
                               .padding(innerPadding)
        ) {
            items(openingsList) { opening ->
                CardButton(
                    opening = opening,
                    onClick = {
                        Log.d("OpeningsScreen", opening.moves.toString())
                        setSelectedOpening(opening)
                        onOpeningClick(opening)
                    }
                )
            }
        }
    }
}

/**
 * Displays a card, with a title and a thumbnail of the opening.
 *
 * @param opening The [Opening].
 * @param onClick What the card does when clicked.
 * @author frigvid
 * @created 2024-10-08
 */
@Composable
private fun CardButton(
    opening: Opening,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF976646))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = opening.title ?: "\uD83D\uDC4B\uD83D\uDE00",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            ChessBoard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                startingPosition = convertPgnToFen(listOf(opening).first().moves ?: ""),
                gameHistory = false,
                gameInteractable = false
            )
        }
    }
}
