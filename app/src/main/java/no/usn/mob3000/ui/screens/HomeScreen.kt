package no.usn.mob3000.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R

/**
 * @author frigvid
 * @created 2024-09-12
 */
@Composable
fun HomeScreen(
    onTrainClick: () -> Unit,
    onPlayClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val buttonSize = (screenWidth * 0.4f).coerceAtMost(192.dp)

    Box(
        modifier = Modifier.fillMaxSize(),
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
