package no.usn.mob3000.ui.components.base

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.domain.enumerate.Destination

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
        title = { if (showTitle) Text(stringResource(currentScreen.title),color = MaterialTheme.colorScheme.onSurface) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
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
