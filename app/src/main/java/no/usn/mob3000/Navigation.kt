package no.usn.mob3000

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.usn.mob3000.ui.components.Navbar
import no.usn.mob3000.ui.components.NavbarItem
import no.usn.mob3000.ui.screens.*

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val navbarItems = listOf(
        NavbarItem("Documentation", "documentation", ImageVector.vectorResource(R.drawable.navbar_documentation)),
        NavbarItem("News", "news", ImageVector.vectorResource(R.drawable.navbar_news)),
        NavbarItem("Home", "home", ImageVector.vectorResource(R.drawable.navbar_home)),
        NavbarItem("Profile", "profile", ImageVector.vectorResource(R.drawable.navbar_profile)),
        NavbarItem("Settings", "settings", ImageVector.vectorResource(R.drawable.navbar_settings))
    )

    Scaffold(
        bottomBar = { Navbar(navController = navController, items = navbarItems) }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("documentation") { DocumentationScreen() }
            composable("news") { NewsScreen() }
            composable("home") { HomeScreen() }
            composable("profile") { ProfileScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}