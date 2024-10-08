package no.usn.mob3000.ui.screens.chess.train.opening

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.usn.mob3000.Viewport

@Composable
fun CreateOpeningScreen() {
    Viewport { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                               .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Create opening screen. Stub.")
        }
    }
}
