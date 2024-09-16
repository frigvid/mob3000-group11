package no.usn.mob3000.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import no.usn.mob3000.R

/**
 * TODO: Find a way around the pointless limitation of not being able to access string resource
 *       values without using a composable.
 *
 * @author frigvid
 * @created 2024-09-16
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: Icon
) {
    data object Documentation : Screen("documentation", "Documentation", Icon.DrawableResourceIcon(R.drawable.navbar_documentation))
    data object News : Screen("news", "News", Icon.DrawableResourceIcon(R.drawable.navbar_news))
    data object Home : Screen("home", "Home", Icon.DrawableResourceIcon(R.drawable.navbar_home))
    data object Profile : Screen("profile", "Profile", Icon.DrawableResourceIcon(R.drawable.navbar_profile))
    data object Settings : Screen("settings", "Settings", Icon.DrawableResourceIcon(R.drawable.navbar_settings))

    companion object {
        val items = listOf(
            Documentation,
            News,
            Home,
            Profile,
            Settings
        )
    }
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
