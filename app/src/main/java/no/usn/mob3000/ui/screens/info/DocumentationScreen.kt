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
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 *
 * Screen for the documentation page. There is a temporary solution for admin to create new documentation, but we'll see what
 * we do about that functionality. {@link no.usn.mob3000.ui.screens.info.CreateDocumentation} does currently have no form, but
 * the form will probably be identical to {@link no.usn.mob3000.ui.screens.info.CreateNews}. Preferable to have one class for handling both
 * of these requests.
 *
 * TODO: Considering abstracting the create form and connecting the cards to the database.
 * TODO: Reuse the code from {@link no.usn.mob3000.ui.screens.info.NewsScreen} for the documentation boxes.
 *
 * @param onFAQButtonClick Callback function to navigate to [FAQScreen].
 * @param onCreateDocumentClick Callback function to navigate to [CreateDocumentation].
 * @author 258030, Eirik
 * @created 2024-09-23
 */

@Composable
fun DocumentationScreen(
    onFAQButtonClick: () -> Unit,
    onCreateDocumentClick: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val width = (screenWidth * 0.85f).coerceAtMost(300.dp)
    val height = (screenHeight * 0.15f).coerceAtMost(250.dp)
    val textBoxH = (screenHeight * 0.25f).coerceAtMost(55.dp)
    val textBoxW = (screenWidth)
    val buttonH = (screenHeight * 0.25f).coerceAtMost(25.dp)
    val buttonW = (screenWidth * 0.8f).coerceAtMost(200.dp)

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DefaultButton,
                onClick = onCreateDocumentClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create documentation")
            }
        }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FaqField(
                    title = stringResource(R.string.docs_title),
                    description = stringResource(R.string.docs_faq_title),
                    width = textBoxW,
                    height = textBoxH
                )
                FaqButton(
                    width = buttonW,
                    height = buttonH,
                    onClick = onFAQButtonClick
                )

                DocBox(
                    title = stringResource(R.string.help_title),
                    description = stringResource(R.string.help_description),
                    body = stringResource(R.string.help_body),
                    color = colorResource(id = R.color.beige_1),
                    width = width,
                    height = height,
                    onClick = { /* TODO: Implement FAQ page */ }
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
fun FaqField(
    title: String,
    description: String,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(height)
            .width(width)
            .padding(top = 8.dp)
            .padding(bottom = 2.dp)
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
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

/**
 * @author 258030, Eirik
 * @created 2024-09-23
 */
@Composable
fun FaqButton(
    title: String = stringResource(R.string.docs_button),
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    color: Color = colorResource(id = R.color.light_brown),
    onClick: () -> Unit
)
{
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


/**
 * @author 258030, Eirik
 * @created 2024-09-23
 */
@Composable
fun DocBox(
    title: String,
    description: String,
    body: String,
    color: Color,
    height: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .height(height)
            .width(width)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
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
            overflow = TextOverflow.Ellipsis
        )
    }
}

