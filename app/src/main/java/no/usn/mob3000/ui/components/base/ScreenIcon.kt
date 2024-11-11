package no.usn.mob3000.ui.components.base

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import no.usn.mob3000.domain.model.base.Icon

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
fun ScreenIcon(
    icon: Icon,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is Icon.ImageVectorIcon -> Icon(
            icon.imageVector,
            contentDescription = null,
            modifier = modifier
        )

        is Icon.DrawableResourceIcon -> Icon(
            painterResource(id = icon.id),
            contentDescription = null,
            modifier = modifier
        )
    }
}
