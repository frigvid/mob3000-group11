package no.usn.mob3000.ui.screens.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * @author 258030, Eirik
 * @created 2024-09-23
 */

@Composable
fun NewsScreen(
    onCreateNewsClick: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val boxSize = (screenWidth * 0.85f).coerceAtMost(300.dp)
    val height = (screenHeight * 0.25f).coerceAtMost(250.dp)

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DefaultButton,
                onClick = onCreateNewsClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create documentation")
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NewsBox(
                    title = stringResource(R.string.breaking_news),
                    description = stringResource(R.string.niemann_intro),
                    body = stringResource(R.string.niemann_report),
                    color = colorResource(id = R.color.beige_1),
                    size = boxSize,
                    height = height,
                    onClick = { /* TODO: Implement single-news-page */ }
                )

                NewsBox(
                    title = stringResource(R.string.shocking_news),
                    description = stringResource(R.string.magnus_intro),
                    body = stringResource(R.string.magnus_report),
                    color = colorResource(id = R.color.beige_1),
                    size = boxSize,
                    height = height,
                    onClick = { /* TODO: Implement single-news-page */ }
                )

            }
        }
    }
}

/**
 * @author 258030, Eirik
 * @created 2024-09-23
 */


@Composable
fun NewsBox(
    title: String,
    description: String,
    body: String,
    color: Color,
    height: androidx.compose.ui.unit.Dp,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .height(height)
            .size(size)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)) // Adds a shadow effect
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) // Adds a 1px border
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .padding(16.dp) // Adds padding inside the box
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            maxLines = 7,
            overflow = TextOverflow.Ellipsis // Truncates the text if it overflows
        )
    }
}

