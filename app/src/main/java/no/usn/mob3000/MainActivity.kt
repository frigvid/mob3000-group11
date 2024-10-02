package no.usn.mob3000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import no.usn.mob3000.ui.theme.ChessbuddyTheme

/**
 * @author frigvid
 * @created 2024-09-02
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChessbuddyTheme { App() }
        }
    }
}