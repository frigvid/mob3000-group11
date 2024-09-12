
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R


@Composable
fun HomePage() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ColorfulButton(
                        color = Color(0xFF3b82f6),
                        icon = R.drawable.home_train,
                        text = stringResource(R.string.home_train_title)
                    )

                    ColorfulButton(
                        color = Color(0xFF22c55e),
                        icon = R.drawable.home_play,
                        text = stringResource(R.string.home_play_title)
                    )

                    ColorfulButton(
                        color = Color(0xFFef4444),
                        icon = R.drawable.home_history,
                        text = stringResource(R.string.home_history_title)
                    )
                }
            }
        }
    )
}
