package no.usn.mob3000.ui.components.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import no.usn.mob3000.ui.Navigation
import no.usn.mob3000.domain.enumerate.Destination
import no.usn.mob3000.ui.LocalNavController
import no.usn.mob3000.ui.screens.chess.train.opening.OpeningsScreen

/**
 * The [Viewport] is a wrapper function for content on a given [Composable] screen. This means that
 * every single screen needs to have its contents wrapped inside the body of this function. See
 * the [Navigation] function documentation for a description of *how* one uses this at the most basic
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
        Destination.INFO,
        Destination.NEWS,
        Destination.HOME,
        Destination.PROFILE,
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
