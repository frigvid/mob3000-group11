package no.usn.mob3000.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Component that displays a small error card.
 *
 * @author frigvid
 * @created 2024-11-03
 */
@Composable
fun Error(
    cardClickable: () -> Unit = {  },
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    cardContainerColor: Color = MaterialTheme.colorScheme.errorContainer
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { cardClickable() },
        colors = CardDefaults.cardColors(cardContainerColor)
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}
