package no.usn.mob3000.ui.components.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R

/**
 * Generic composable for displaying individual rows from the database. Show the title, summary, main content, creation/modification and status.
 * Since the screens are so similar, the only real difference is what data is being displayed. Because the data is needed for further navigation and editing,
 * it has to be passed as parameters.
 *
 * @param modifier Modifier for the composable.
 * @param title The title of the row.
 * @param summary The summary of the row.
 * @param content The main content of the row.
 * @param createdAt The creation date of the row.
 * @param modifiedAt The modification date of the row.
 * @param isPublished Whether the row is published or not.
 *
 * @author 258030
 * @contributor frigvid
 * @created 2024-10-30
 */
@Composable
fun ContentDisplay(
    modifier: Modifier = Modifier,
    title: String,
    summary: String?,
    content: String?,
    createdAt: Long? = null,
    modifiedAt: Long? = null,
    isPublishedText: String = ""
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            summary?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            content?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            createdAt?.let {
                Text(
                    text = formatInstant(it, isCreated = true),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            modifiedAt?.let {
                Text(
                    text = formatInstant(it, isCreated = false),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            if (isPublishedText.isNotBlank()) {
                Text(
                    text = stringResource(R.string.info_item_details_status_prefix) + ": $isPublishedText",
                    fontSize = 12.sp,
                )
            }
        }
    }
}
