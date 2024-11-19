package no.usn.mob3000.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport

/**
 * The home screen.
 *
 * @param onTrainClick Callback function to navigate to the openings screen.
 * @param onPlayClick Callback function to navigate to the chessboard screen.
 * @param onHistoryClick Callback function to navigate to the game history screen.
 * @param openingsStartPeriodicUpdates Callback function to schedule an update for openings.
 * @param groupsStartPeriodicUpdates Callback function to schedule an update for repertoires/groups.
 * @author frigvid
 * @created 2024-09-12
 */
@Composable
fun HomeScreen(
    onTrainClick: () -> Unit,
    onPlayClick: () -> Unit,
    onHistoryClick: () -> Unit,
    openingsStartPeriodicUpdates: () -> Unit,
    groupsStartPeriodicUpdates: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val buttonSize = (screenWidth * 0.4f).coerceAtMost(192.dp)

    /* Schedule background check for openings. */
    LaunchedEffect(Unit) {
        openingsStartPeriodicUpdates()
        groupsStartPeriodicUpdates()
    }

    Viewport { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                               .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HomePageButton(
                    text = stringResource(R.string.home_train_title),
                    icon = R.drawable.home_train,
                    color = Color(0xFF3B82F6),
                    size = buttonSize,
                    onClick = onTrainClick
                )
                HomePageButton(
                    text = stringResource(R.string.home_play_title),
                    icon = R.drawable.home_play,
                    color = Color(0xFF22C55E),
                    size = buttonSize,
                    onClick = onPlayClick
                )
                HomePageButton(
                    text = stringResource(R.string.home_history_title),
                    icon = R.drawable.home_history,
                    color = Color(0xFFEF4444),
                    size = buttonSize,
                    onClick = onHistoryClick
                )
            }
        }
    }
}

/**
 * @author frigvid
 * @contributor Husseinabdulameer11
 * @created 2024-09-16
 */
@Composable
fun HomePageButton(
    text: String,
    icon: Int,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = color),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(size)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(size * 0.5f)
            )
        }
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
