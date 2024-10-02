package no.usn.mob3000

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
import no.usn.mob3000.ui.screens.chess.train.CreateGroupScreen
import no.usn.mob3000.ui.screens.chess.train.CreateOpeningScreen
import no.usn.mob3000.ui.screens.info.FAQScreen
import no.usn.mob3000.ui.screens.chess.train.GroupsScreen
import no.usn.mob3000.ui.screens.chess.train.OpeningsScreen
import no.usn.mob3000.ui.theme.NavbarBackground
import no.usn.mob3000.ui.theme.NavbarButtonSelected

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

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
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.name,
            modifier = Modifier.fillMaxSize()
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
 * This function serves as the default viewport that screens need to implement.
 *
 * @author frigvid
 * @created 2024-10-01
 */
@Composable
fun Viewport(
    topBar: @Composable (
        currentScreen: Screen,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier,
        roots: List<Screen>,
        showTitle: Boolean,
        topBarActions: @Composable (RowScope.() -> Unit)
    ) -> Unit = { currentScreen, canNavigateBack, navigateUp, modifier, roots, showTitle, topBarActions ->
        TopNavbar(
            currentScreen = currentScreen,
            canNavigateBack = canNavigateBack,
            navigateUp = navigateUp,
            modifier = modifier,
            roots = roots,
            showTitle = showTitle,
            topBarActions = topBarActions
        )
    },
    bottomBar: @Composable (
        rootEntries: List<Screen>,
        currentDestination: NavDestination?,
        onNavigate: (String) -> Unit
    ) -> Unit = { rootEntries, currentDestination, onNavigate ->
        BottomNavbar(
            rootEntries = rootEntries,
            currentDestination = currentDestination,
            onNavigate = onNavigate
        )
    },
    hideTitle: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    topBarActions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navController = LocalNavController.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val currentScreen = Screen.valueOf(
        currentBackStackEntry?.destination?.route ?: Screen.HOME.name
    )

    val rootEntries = listOf(
        Screen.DOCUMENTATION,
        Screen.NEWS,
        Screen.HOME,
        Screen.PROFILE,
        Screen.SETTINGS
    )

    Scaffold(
        topBar = {
            topBar(
                currentScreen,
                navController.previousBackStackEntry != null,
                { navController.navigateUp() },
                Modifier,
                rootEntries,
                hideTitle,
                topBarActions
            )
        },
        bottomBar = {
            bottomBar(
                rootEntries,
                currentDestination
            ) { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        floatingActionButton = floatingActionButton
    ) { innerPadding -> content(innerPadding) }
}

/**
 * This enum class defines whether a route exists or not.
 *
 * @param title The title, usually shown in the TopAppBar.
 * @param icon The icon, primarily shown in the BottomAppBar.
 * @author frigvid
 * @created 2024-09-24
 */
enum class Screen(@StringRes val title: Int, val icon: Icon? = null) {
    DOCUMENTATION(title = R.string.docs_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_documentation)),
    FAQ(title = R.string.faq_title),
    NEWS(title = R.string.news_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_news)),
    HOME(title = R.string.home_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_home)),
    PROFILE(title = R.string.profile_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_profile)),
    SETTINGS(title = R.string.settings_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_settings)),
    OPENINGS(title = R.string.openings_title),
    OPENINGS_CREATE(title = R.string.openings_create_title),
    GROUPS(title = R.string.groups_title),
    GROUPS_CREATE(title = R.string.groups_create_title),
    PLAY(title = R.string.home_play_title),
    HISTORY(title = R.string.home_history_title),
    AUTH_LOGIN(title = R.string.auth_login_title),
    AUTH_CREATE(title = R.string.auth_createUser_title),
    AUTH_FORGOT(title = R.string.auth_forgotPassword_title),
    AUTH_RESET(title = R.string.auth_resetPassword_title)
}

/**
 * This is the navigation bar wrapper at the top of the viewport. Primarily, it serves
 * the purpose of displaying the title of the page, and providing a navigation point
 * back to the root page.
 *
 * TODO: Show button title/label when button is pressed-and-held for a length of time.
 *       Once released, it should not fire a click-event. That'd suck.
 *
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
    roots: List<Screen>,
    showTitle: Boolean = true,
    topBarActions: @Composable (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        title = { if (showTitle) Text(stringResource(currentScreen.title)) },
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
        },
        actions = topBarActions
    )
}

@Composable
fun BottomNavbar(
    rootEntries: List<Screen>,
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
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
