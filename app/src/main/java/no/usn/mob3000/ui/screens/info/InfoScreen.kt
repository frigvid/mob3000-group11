package no.usn.mob3000.ui.screens.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 * This screen serves as an entry point for the various other documentation screens.
 *
 * @see [DocumentationScreen]
 * @see [FAQScreen]
 * @see [AboutUsScreen]
 * @author frigvid
 * @created 2024-09-12
 */
@Composable
fun InfoScreen(
    onAboutUsClick: () -> Unit,
    onDocumentationClick: () -> Unit,
    onFAQClick: () -> Unit,
) {
    Viewport { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            /* TODO: 3 buttons that go to the different documentation pages. */
        }
    }
}