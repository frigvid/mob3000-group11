package no.usn.mob3000.ui.screens.info.docs

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
 *
 * @author 258030 (Eirik)
 * @created 2024-10-10
 */
@Composable
fun CreateDocumentationScreen() {
    Viewport { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.docs_create_title))
        }
    }
}