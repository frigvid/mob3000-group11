package no.usn.mob3000.domain.model.base

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

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
