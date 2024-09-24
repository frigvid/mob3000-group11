package no.usn.mob3000.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.usn.mob3000.R
import no.usn.mob3000.ui.screens.info.DocumentationScreen
import no.usn.mob3000.ui.screens.HomeScreen
import no.usn.mob3000.ui.screens.info.NewsScreen
import no.usn.mob3000.ui.screens.ProfileScreen
import no.usn.mob3000.ui.screens.SettingsScreen
import no.usn.mob3000.ui.screens.auth.CreateUserScreen
import no.usn.mob3000.ui.screens.auth.ForgotPasswordScreen
import no.usn.mob3000.ui.screens.auth.LoginScreen
import no.usn.mob3000.ui.screens.auth.ResetPasswordScreen
import no.usn.mob3000.ui.screens.chess.HistoryScreen
import no.usn.mob3000.ui.screens.chess.PlayScreen
import no.usn.mob3000.ui.screens.info.FAQScreen
import no.usn.mob3000.ui.screens.chess.train.GroupsScreen
import no.usn.mob3000.ui.screens.chess.train.OpeningsScreen
import no.usn.mob3000.ui.theme.NavbarBackground
import no.usn.mob3000.ui.theme.NavbarButtonSelected

/**
 * This file is the main navigation point for the application, and serves as a place
 * for everything related to navigation.
 *
 * This function serves as the primary handler for actual route navigation, but
 * the Screen enum class defines if a route exists or not.
 *
 * @param navController The navigation controller.
 * @see Screen
 * @see TopNavbar
 * @see ScreenIcon
 * @see Icon
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.HOME.name
    )

    /* Since the navigation bar should only have some of the routes added,
     * this is the somewhat dirty work-around. We could just make a second
     * route enum class for base routes, and honestly, we probably should,
     * since regular routes have no need for an icon.
     */
    val rootEntries = listOf(
        Screen.DOCUMENTATION,
        Screen.NEWS,
        Screen.HOME,
        Screen.PROFILE,
        Screen.SETTINGS
    )

    Scaffold(
        topBar = {
            TopNavbar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                roots = rootEntries
            )
        },
        bottomBar = {
            /* TODO: Extract this. */
            BottomAppBar(
                containerColor = NavbarBackground
            ) {
                rootEntries.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            screen.icon?.let {
                                ScreenIcon(
                                    it,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        label = null, // Alternatively: label = { Text(stringResource(screen.title)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.name } == true,
                        onClick = {
                            navController.navigate(screen.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            indicatorColor = NavbarButtonSelected
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = Screen.DOCUMENTATION.name) { DocumentationScreen() }
            composable(route = Screen.FAQ.name) { FAQScreen() }
            composable(route = Screen.NEWS.name) { NewsScreen() }
            composable(route = Screen.HOME.name) {
                HomeScreen(
                    onTrainClick = { navController.navigate(Screen.OPENINGS.name) },
                    onPlayClick =  { navController.navigate(Screen.PLAY.name) },
                    onHistoryClick =  { navController.navigate(Screen.HISTORY.name) }
                )
            }
            composable(route = Screen.OPENINGS.name) { OpeningsScreen() }
            composable(route = Screen.GROUPS.name) { GroupsScreen() }
            composable(route = Screen.PLAY.name) { PlayScreen() }
            composable(route = Screen.HISTORY.name) { HistoryScreen() }
            composable(route = Screen.PROFILE.name) { ProfileScreen() }
            composable(route = Screen.SETTINGS.name) { SettingsScreen() }
            composable(route = Screen.AUTH_LOGIN.name) { LoginScreen() }
            composable(route = Screen.AUTH_CREATE.name) { CreateUserScreen() }
            composable(route = Screen.AUTH_FORGOT.name) { ForgotPasswordScreen() }
            composable(route = Screen.AUTH_RESET.name) { ResetPasswordScreen() }
        }
    }
}

/**
 * This enum class defines whether a route exists or not.
 *
 * @param title The title, usually shown in the TopAppBar.
 * @param icon The icon, primarily shown in the BottomAppBar.
 * @author frigvid
 * @created 2024-09-24
 */
enum class Screen(@StringRes val title: Int, val icon: Icon?) {
    DOCUMENTATION(title = R.string.docs_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_documentation)),
    FAQ(title = R.string.faq_title, icon = null),
    NEWS(title = R.string.news_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_news)),
    HOME(title = R.string.home_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_home)),
    PROFILE(title = R.string.profile_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_profile)),
    SETTINGS(title = R.string.settings_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_settings)),
    OPENINGS(title = R.string.openings_title, icon = null),
    GROUPS(title = R.string.groups_title, icon = null),
    PLAY(title = R.string.home_play_title, icon = null),
    HISTORY(title = R.string.home_history_title, icon = null),
    AUTH_LOGIN(title = R.string.auth_login_title, icon = null),
    AUTH_CREATE(title = R.string.auth_createUser_title, icon = null),
    AUTH_FORGOT(title = R.string.auth_forgotPassword_title, icon = null),
    AUTH_RESET(title = R.string.auth_resetPassword_title, icon = null)
}

/**
 * This is the navigation bar at the top of the viewport. Primarily, it serves
 * the purpose of displaying the title of the page, and providing a navigation
 * point back to the root page.
 *
 * TODO: Show button title/label when button is pressed-and-held for a length of time.
 *       Once released, it should not fire a click-event. That'd suck.
 * @param currentScreen The current route.
 * @param canNavigateBack This decides if you have a way to navigate back to the root.
 *                        Note that if a Screen enums is given through <roots>, this
 *                        navigation button will always be hidden.
 * @param navigateUp NavController's function to try to find the "root" Screen.
 * @param modifier Any modifiers, as necessary.
 * @param roots This accepts a list of Screen enums, used to decide the "root" pages.
 * @author frigvid
 * @created 2024-09-16
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavbar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    roots: List<Screen>
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = NavbarBackground
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack && currentScreen !in roots) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,

                        /* TODO: Extract this resource, or find a better
                         *       dynamic/semantic way to give end-users context.
                         */
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

/**
 * Similarly to the Icon class, this function serves the purpose of actually
 * returning the correct type of Icon so it can be drawn to the compositor.
 *
 * @param icon Either an ImageVector or Drawable resource from the Icon class.
 * @param modifier Any modifiers, as necessary.
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

/**
 * A simple class to allow for both <Icon> icons and Drawables.
 *
 * @author frigvid
 * @created 2024-09-16
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
