package no.usn.mob3000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.jan.supabase.gotrue.handleDeeplinks
import no.usn.mob3000.data.SecureEnvManager
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.ui.Navigation
import no.usn.mob3000.ui.theme.ChessbuddyTheme

/**
 * The main activity for the application, essentially the entry point.
 * Also handles deep links
 *
 * @author frigvid
 * @contributor Anarox
 * @created 2024-09-02
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        SecureEnvManager.initializeEnvVariables(applicationContext)
        SupabaseClientWrapper.initialize(applicationContext)

        SupabaseClientWrapper.getClient().handleDeeplinks(intent)

        super.onCreate(savedInstanceState)

        setContent {
            ChessbuddyTheme { Navigation() }
        }
    }
}
