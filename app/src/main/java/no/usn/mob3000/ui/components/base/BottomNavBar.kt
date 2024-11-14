package no.usn.mob3000.ui.components.base

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import no.usn.mob3000.domain.enumerate.Destination
import no.usn.mob3000.ui.theme.NavbarBackground
import no.usn.mob3000.ui.theme.NavbarButtonSelected

/**
 * This is the bottom navigation bar, it's used in the [Viewport] by default. It is a wrapper for the
 * [Composable] [BottomAppBar], and is used to set default values for the bottom navigation bar.
 *
 * In most cases, you'd never want to directly call this. You *can* use the [Viewport]'s `bottomBar`
 * parameter to replace it, but you're probably going to have a bad time if you do. At least if you
 * don't define some *other* way to get back to a screen that implements the default. A regular
 * navigate up should do the trick, but I thought I might as well warn whoever wants to directly
 * use this beforehand.
 *
 * @param roots These are the "root" pages. See [Viewport]'s `rootEntries`. Without these even the
                the destinations the buttons on this bottom navigation bar leads to, will show an
                ineffectual back button.
 * @param currentDestination
 * @param onNavigate
 * @author frigvid
 * @created 2024-10-02
 */
@Composable
fun BottomNavbar(
    roots: List<Destination>,
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    BottomAppBar(
        containerColor = NavbarBackground
    ) {
        roots.forEach { screen ->
            NavigationBarItem(
                icon = {
                    screen.icon?.let {
                        ScreenIcon(
                            it,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = null,
                selected = currentDestination?.hierarchy?.any { it.route == screen.name } == true,
                onClick = { onNavigate(screen.name) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = NavbarButtonSelected
                )
            )
        }
    }
}
