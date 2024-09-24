package no.usn.mob3000.ui.screens.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R

/**
 * This is the FAQ page, where frequently asked questions will be displayed.
 *
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun FAQScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.faq_title))
    }
}