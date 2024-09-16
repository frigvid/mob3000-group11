package no.usn.mob3000.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.usn.mob3000.ui.screens.DocumentationScreen
import no.usn.mob3000.ui.screens.HomeScreen
import no.usn.mob3000.ui.screens.NewsScreen
import no.usn.mob3000.ui.screens.ProfileScreen
import no.usn.mob3000.ui.screens.SettingsScreen

/**
 * @author frigvid
 * @created 2024-09-16
 */
@Composable
fun NavbarHost (
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        Screen.items.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    Screen.Documentation -> DocumentationScreen()
                    Screen.News -> NewsScreen()
                    Screen.Home -> HomeScreen()
                    Screen.Profile -> ProfileScreen()
                    Screen.Settings -> SettingsScreen()
                }
            }
        }
    }
}
