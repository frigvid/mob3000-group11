package no.usn.mob3000.ui.components.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.ui.theme.DefaultListItemBackground

/**
 * Composable function to display a single content item. Purely an UI component and is made abstract
 * since there are three screens that has very similar structure.
 *
 * @param title The title of the content item.
 * @param summary The summary of the content item.
 * @param isPublished A flag indicating whether the content item is published or not.
 * @param onClick The action to perform when the content item is clicked.
 * @param borderStroke is to indicate whether the content item is published or not.
 * @author 258030
 * @created 2024-10-31
 */
@Composable
fun ContentItem(
    title: String,
    summary: String? = null,
    isPublished: Boolean = true,
    onClick: () -> Unit,
    borderStroke: BorderStroke? = if (!isPublished) BorderStroke(2.dp, Color(0xFFFF0000)) else null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground),
        border = borderStroke
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            summary?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

/**
 * Generic lazy column with padding, intended to reduce redundancy in the compose screens as they all uses the same layout.
 *
 * @param innerPadding The padding to be applied to the content.
 * @param content The content to be displayed in the lazy column.
 */
@Composable
fun PaddedLazyColumn(
    innerPadding: PaddingValues,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}
