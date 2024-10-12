package no.usn.mob3000

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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

/**
 * The LocalNavController was borne out of necessity, to allow for a more dynamic UI where
 * individual screens can define parts of the viewport. Normally, one wouldn't pass around the
 * NavController directly, and that's why we're it's done using local composition, to avoid
 * callback hell.
 *
 * As an aside, this still shouldn't be used to pass around to individual screens unless absolutely
 * necessary. In most cases, you merely need to use "navController.navigate()".
 *
 * @author frigvid
 * @created 2024-10-02
 */
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

/**
 * The App function serves as the main navigation point for the application. This is where you
 * define an actual route, its underlying destinations, and whatever else is necessary.
 *
 * To define a route, three things are necessary.
 * 1. You need to create a file called <something>Screen.kt. This is under ui/screens/.
 * 2. You need to add a destination definition to the enum class [Destination].
 * 3. You need to define a composable route definition within the NavHost.
 *
 * When creating a new [Composable] screen, it's important to remember to use th [Viewport] as
 * the root of the screen. If you don't use it, you're going to have a bad time. The reason you'd
 * want to use it, is to allow for individual screens to replace the topBars or bottomBars, or
 * just add certain parts to the topBar without fully replacing it. Below is a very, very basic
 * example of a given Jetpack Compose screen.
 * ```
 * @Composable
 * fun CreateOpeningScreen() {
 *     Viewport { innerPadding ->
 *         Box(
 *             modifier = Modifier.fillMaxSize()
 *                                .padding(innerPadding),
 *             contentAlignment = Alignment.Center
 *         ) {
 *             Text("Create opening screen. Stub.")
 *         }
 *     }
 * }
 * ```
 *
 * For step 2, you'll need to register the destination definition in the enum class [Destination].
 *
 * Finally, to define the actual navigation logic, you'll need to create a composable route
 * definition. This consists of 2 things, at the most basic level. A parameter where you give the
 * destination definition from [Destination], and a body, where you call for the function you made in
 * step 1. Below is an example of this:
 * ```
 * composable(route = Screen.DOCUMENTATION.name) { DocumentationScreen() }
 * ```
 *
 * As you can see, the parameter is "route", and it uses the the string value of the
 * [Destination.DOCUMENTATION]. This is what decides where it goes, and if it has an icon.
 * Within the body, you call the function for the screen you made in step 1.
 *
 * @param navController The navigation controller.
 * @see Destination
 * @see Viewport
 * @see ScreenIcon
 * @see Icon
 * @author frigvid
 * @contributors Routes: Anarox, Markus
 * @created 2024-09-24
 */
@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Destination.HOME.name,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = Destination.DOCUMENTATION.name) { DocumentationScreen() }
            composable(route = Destination.FAQ.name) { FAQScreen() }
            composable(route = Destination.NEWS.name) { NewsScreen() }
            composable(route = Destination.HOME.name) {
                HomeScreen(
                    onTrainClick = { navController.navigate(Destination.OPENINGS.name) },
                    onPlayClick =  { navController.navigate(Destination.PLAY.name) },
                    onHistoryClick =  { navController.navigate(Destination.HISTORY.name) }
                )
            }
            composable(route = Destination.OPENINGS.name) {
                OpeningsScreen(
                    onGroupsClick = { navController.navigate(Destination.GROUPS.name) },
                    onCreateOpeningClick = { navController.navigate(Destination.OPENINGS_CREATE.name) }
                )
            }
            composable(route = Destination.OPENINGS_CREATE.name) { CreateOpeningScreen() }
            composable(route = Destination.GROUPS.name) {
                GroupsScreen(
                    onCreateGroupClick = { navController.navigate(Destination.GROUPS_CREATE.name) },
                    onReturnToOpeningClick = { navController.navigate(Destination.OPENINGS.name) }
                )
            }
            composable(route = Destination.GROUPS_CREATE.name) { CreateGroupScreen() }
            composable(route = Destination.PLAY.name) { PlayScreen() }
            composable(route = Destination.HISTORY.name) { HistoryScreen() }
            composable(route = Destination.PROFILE.name) {
                ProfileScreen(
                    onLogin = { navController.navigate(Destination.AUTH_LOGIN.name) }
                )
            }
            composable(route = Destination.SETTINGS.name) { SettingsScreen() }
            composable(route = Destination.AUTH_LOGIN.name) {
                LoginScreen(
                    onCreateUserClick = { navController.navigate(Destination.AUTH_CREATE.name)},
                    onLoginClick = { navController.navigate(Destination.HOME.name) },
                    onForgotPasswordClick = { navController.navigate(Destination.AUTH_FORGOT.name) }
                )
            }
            composable(route = Destination.AUTH_CREATE.name) {
                CreateUserScreen(
                    onSignIn = { navController.navigate(Destination.HOME.name) },
                    onLogin = { navController.navigate(Destination. AUTH_LOGIN.name) }
                )
            }
            composable(route = Destination.AUTH_FORGOT.name) {
                ForgotPasswordScreen(
                    onResetPassword = { navController.navigate(Destination.AUTH_RESET.name) }
                )
            }
            composable(route = Destination.AUTH_RESET.name) {
                ResetPasswordScreen(
                    onReset = { navController.navigate(Destination.HOME.name) },
                )
            }
        }
    }
}

/**
 * The [Viewport] is a wrapper function for content on a given [Composable] screen. This means that
 * every single screen needs to have its contents wrapped inside the body of this function. See
 * the [App] function documentation for a description of *how* one uses this at the most basic
 * level.
 *
 * The parameter list is not required to be used. You can wrap a screen's contents within this
 * function's body, without adding or changing any parameters. The parameters for
 * [topBar] and [bottomBar] both have default values for the [TopNavbar] and [BottomNavbar]
 * functions. Which means it's not necessary to override these.
 *
 * However, you definitely can. It shouldn't be needed though. You can add a [floatingActionButton]
 * and however many buttons you can fit inside the [topBar] using [topBarActions]. You can also
 * toggle whether or not the title is shown in the [topBar] by setting [showTitle] to false.
 *
 * Below is an example based on [OpeningsScreen], that shows a basic way to create a
 * [FloatingActionButton] and [IconButton] for the [floatingActionButton] and [topBarActions]
 * parameters.
 *
 * ```
 * Viewport (
 *         floatingActionButton = {
 *             FloatingActionButton(onClick = onCreateOpeningClick) {
 *                 Icon(Icons.Default.Add, contentDescription = "Create opening")
 *             }
 *         },
 *         topBarActions = {
 *             IconButton(onClick = onGroupsClick) {
 *                 Icon(Icons.Default.PlayArrow, contentDescription = "Groups")
 *             }
 *         }
 *     ) { innerPadding ->
 *         /* The body. */
 *         Box (
 *             Modifier.padding(innerPadding)
 *         ) {
 *
 *         }
 *     }
 * ```
 *
 * TODO: Investigate and fix eventual UX problems caused by navigating in circles. E.g. if you have
 *       destination1 and destination2, and create code that allows you to go to destination1 ->
 *       destination2 -> destination1 -> destination2, *without* using the navigate up functionality
 *       from the navController. You'll have to go through each destination travelled to in reverse
 *       order, even when it doesn't make sense. Currently, you're traveling like this: destination1
 *       <- destination2 <- destination1 <- destination2 when using the navigate up button. When it
 *       should be like this: destination0(root) <- destination1 <- destination2.
 *
 * @param topBar This is the top navigation bar. By default, this is [TopNavbar].
 * @param bottomBar This is the bottom navigation bar. By default, this is [BottomNavbar].
 * @param showTitle This is a boolean that hides or displays the title. By default this is true.
 * @param floatingActionButton This is floating action button in the bottom right corner of the
                               screen. It's recommended to use [FloatingActionButton] as the button
                               type. You can define multiple buttons, but it wasn't really designed
                               with this in mind, and may be a bit wonky if you decide to do so.
 * @param topBarActions This is a [RowScope] and where you define the buttons in the [TopNavbar].
                        You can define multiple buttons.
 * @param content This is the body of the function. You shouldn't need to pass anything to this
                  parameter, simply write whatever content is necessary within the actual body.
                  Feel free to use this if you want to, though, but you're on your own, man.
 * @author frigvid
 * @created 2024-10-01
 */
@Composable
fun Viewport(
    topBar: @Composable (
        currentScreen: Destination,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier,
        roots: List<Destination>,
        title: Boolean,
        actions: @Composable (RowScope.() -> Unit)
    ) -> Unit = { currentScreen, canNavigateBack, navigateUp, modifier, roots, title, actions ->
        TopNavbar(
            currentScreen = currentScreen,
            canNavigateBack = canNavigateBack,
            navigateUp = navigateUp,
            modifier = modifier,
            roots = roots,
            showTitle = title,
            topBarActions = actions
        )
    },
    bottomBar: @Composable (
        rootEntries: List<Destination>,
        currentDestination: NavDestination?,
        onNavigate: (String) -> Unit
    ) -> Unit = { rootEntries, currentDestination, onNavigate ->
        BottomNavbar(
            roots = rootEntries,
            currentDestination = currentDestination,
            onNavigate = onNavigate
        )
    },
    showTitle: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    topBarActions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navController = LocalNavController.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val currentScreen = Destination.valueOf(
        currentBackStackEntry?.destination?.route ?: Destination.HOME.name
    )

    val rootEntries = listOf(
        Destination.DOCUMENTATION,
        Destination.NEWS,
        Destination.HOME,
        Destination. PROFILE,
        Destination.SETTINGS
    )

    Scaffold(
        topBar = {
            topBar(
                currentScreen,
                navController.previousBackStackEntry != null,
                { navController.navigateUp() },
                Modifier,
                rootEntries,
                showTitle,
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
 * This enum class defines the destination of a given route, and whether or not it has an icon.
 *
 * A destination definition, is quite simple. You need to add a title within a `values/strings.xml`
 * file, and whichever localizations there are. And you need to define an icon, if applicable.
 *
 * Here is an example of a root destination, and a regular route destination.
 * ```
 * SOME_ROOT_DESTINATION(title = R.string.some_route_destination, icon = Icon.DrawableResourceIcon(R.drawable.navbar_some_root_destination)),
 * SOME_DESTINATION(title = R.string.some_destination)
 * ```
 *
 * The destinations chosen for the [BottomNavbar], are controlled via a list called `rootEntries`
 * inside the [Viewport] function.
 *
 * @param title The title for the destination. For example, this could be used in the [TopNavbar].
 * @param icon The icon for the destination. Primarily used in the [BottomNavbar].
 * @author frigvid
 * @created 2024-09-24
 */
enum class Destination(@StringRes val title: Int, val icon: Icon? = null) {
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
 * This is the top navigation bar, used in the [Viewport] function. It is a wrapper for the
 * [Composable] [TopAppBar], and is used to set default values for the top navigation bar.
 *
 * You're free to call on this as necessary. However, keep in mind that you'd usually only
 * use this in the capacity of [Viewport]'s `topBar` parameter. Nothing is actually stopping
 * you from using this directly, though.
 *
 * TODO: Show button title/label when button is pressed-and-held for a length of time.
 *       Once released, it should not fire a click-event. That'd suck.
 *
 * @param currentScreen The current destination the user is on.
 * @param canNavigateBack This decides if you have a way to navigate back to the root.
                          Note that if a Screen enums is given through [roots], this
                          navigation button will always be hidden.
 * @param navigateUp NavController's function to try to find the "root" screen before the [currentScreen].
 * @param modifier Any modifiers, as necessary.
 * @param roots This accepts a list of [Destination] enums, used to decide the "root" pages.
 * @param showTitle Whether or not to show the title. Boolean, default is true.
 * @param topBarActions Any buttons to add. See [Viewport] for more information.
 * @author frigvid
 * @created 2024-09-16
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavbar(
    currentScreen: Destination,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    roots: List<Destination>,
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
