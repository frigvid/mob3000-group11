package no.usn.mob3000.ui.screens.chess

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.screens.chess.opening.OpeningDetailsScreen
import java.text.SimpleDateFormat
import java.util.*

/**
 * This is the history screen, and should display the user's previous games.
 *
 * From here, they should be able to expand an entry, and be able to step
 * back and forwards through their game.
 *
 * NOTE: This relies on the user having fetched all openings already. Currently, the user needs to
 *       visit the openings page and wait for things to load for this to work. When re-implemented,
 *       this should be fetched silently to avoid issues like this.
 *
 * @param setSelectedOpening ViewModel function to set the selected opening.
 * @param getOpeningById ViewModel function to get an opening by ID.
 * @param onOpeningDetailsClick Callback function to navigate to [OpeningDetailsScreen].
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun HistoryScreen(
    setSelectedOpening: (Opening) -> Unit,
    getOpeningById: (String) -> Opening?,
    onOpeningDetailsClick: () -> Unit
) {
    /* Fake data for sprint 1. */
    val fakeHistoryItems = remember {
        listOf(
            HistoryItem(
                "1",
                Date(),
                "Win",
                "0976138f-46fe-45b5-aa7e-9272f4dbab87"
            ),
            HistoryItem(
                "2",
                Date(System.currentTimeMillis() - 86400000),
                "Loss",
                null
            ),
            HistoryItem(
                "3",
                Date(System.currentTimeMillis() - 172800000),
                "Draw",
                "0976138f-46fe-45b5-aa7e-9272f4dbab87"
            )
        )
    }

    Viewport { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(fakeHistoryItems) { item ->
                HistoryListItem(
                    item,
                    onViewDetailsClick = {
                        if (item.openingId != null) {
                            getOpeningById(item.openingId)?.let { it1 -> setSelectedOpening(it1) }

                            /* NOTE: Won't show any details if you haven't visited the openings
                             *       screen first.
                             */
                            onOpeningDetailsClick()
                        }
                    }
                )
            }
        }
    }
}

/* TODO: Extract to data layer. */
data class HistoryItem(
    val id: String,
    val date: Date,
    val result: String,
    val openingId: String?
)

/**
 * A function that displays a single item in the history list of chess plays.
 *
 * This component creates an expandable card that shows basic information about a chess play
 * when collapsed, and more detailed information when expanded. It uses Material 3 design
 * components and follows the app's theming guidelines.
 *
 * @param item The [HistoryItem] object containing the data to be displayed.
 * @param onViewDetailsClick A lambda function that is called when the "View Opening" button is
 *                           clicked. It receives the opening ID as a parameter.
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun HistoryListItem(
    item: HistoryItem,
    onViewDetailsClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            Locale.getDefault()
                        ).format(item.date),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.history_result, item.result),
                        fontSize = 14.sp
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.placeholder_chess),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { /* TODO: Implement step backward logic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    ) { Text(stringResource(R.string.history_step_backward)) }

                    Button(
                        onClick = { /* TODO: Implement step forward logic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    ) { Text(stringResource(R.string.history_step_forward)) }
                }

                if (item.openingId != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onViewDetailsClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) { Text(stringResource(R.string.history_view_opening)) }
                }
            }
        }
    }
}
