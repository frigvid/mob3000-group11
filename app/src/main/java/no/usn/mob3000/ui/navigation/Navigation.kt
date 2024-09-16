package no.usn.mob3000.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.usn.mob3000.ui.components.Navbar
import no.usn.mob3000.ui.theme.DefaultBackground

/**
 * @author frigvid
 * @created 2024-09-12
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = Screen.items.find { it.route == currentRoute } ?: Screen.Home

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DefaultBackground
    ) {
        Scaffold(
            /* TODO: Consider using the top bar for having per-screen buttons.
             *       E.g., the documentation page could have Docs and FAQ, and default to Docs.
             *       And the Training page could have buttons to switch between openings and groups.
             */
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen.title, color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF7F563B)
                    )
                )
            },
            bottomBar = {
                Navbar(
                    navController = navController,
                    items = Screen.items
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                paddingValues = innerPadding
            )
        }
    }
}