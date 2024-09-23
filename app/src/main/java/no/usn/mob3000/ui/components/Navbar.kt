package no.usn.mob3000.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import no.usn.mob3000.ui.navigation.Icon
import no.usn.mob3000.ui.navigation.Screen
import no.usn.mob3000.ui.theme.NavbarBackground
import no.usn.mob3000.ui.theme.NavbarButtonSelected

/**
 * TODO: Show button title/label when button is pressed-and-held for a length of time.
 *       Once released, it should not fire a click-event. That'd suck.
 *
 * @author frigvid
 * @created 2024-09-16
 */

@Composable
fun Navbar(
    navController: NavController,
    items: List<Screen>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = NavbarBackground
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    ScreenIcon(
                        screen.icon,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.semantics {
                    contentDescription = screen.title
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = NavbarButtonSelected
                )
            )
        }
    }
}

/**
 * This serves only the pathetic purpose of allowing both resource icons, and package icons, to be
 * used by the navbar.
 *
 * @author frigvid
 * @created 2024-09-16
 */
@Composable
private fun ScreenIcon(icon: Icon, modifier: Modifier = Modifier) {
    when (icon) {
        is Icon.ImageVectorIcon -> Icon(icon.imageVector, contentDescription = null, modifier = modifier)
        is Icon.DrawableResourceIcon -> Icon(painterResource(id = icon.id), contentDescription = null, modifier = modifier)
    }
}
