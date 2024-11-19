package no.usn.mob3000.ui.screens.info

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport

/**
 * This screen serves as an entry point for the various other documentation screens.
 *
 * @param onAboutUsClick Callback function to navigate to [AboutUsScreen].
 * @param onDocumentationClick Callback function to navigate to [DocumentationScreen].
 * @param onFAQClick Callback function to navigate to [FAQScreen].
 * @see [DocumentationScreen]
 * @see [FAQScreen]
 * @see [AboutUsScreen]
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
fun InfoScreen(
    onAboutUsClick: () -> Unit,
    onDocumentationClick: () -> Unit,
    onFAQClick: () -> Unit,
) {
    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoButton(
                text = stringResource(R.string.about_us_title),
                onClick = onAboutUsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoButton(
                text = stringResource(R.string.documentation_title),
                onClick = onDocumentationClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoButton(
                text = stringResource(R.string.faq_title),
                onClick = onFAQClick
            )
        }
    }
}

/**
 * Re-usable "large" button component.
 *
 * @param text The text to display on the button.
 * @param onClick The action(s) to execute when clicked.
 * @author frigvid
 * @created 2024-10-09
 */
@Composable
private fun InfoButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) { Text(text) }
}
