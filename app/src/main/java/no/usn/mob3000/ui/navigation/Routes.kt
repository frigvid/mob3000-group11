package no.usn.mob3000.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import no.usn.mob3000.R

sealed class Routes(val title: String, val route: String, val icon: ImageVector) {
    data object Documentation : Routes(
        "Documentation",
        "documentation",
        ImageVector.vectorResource(R.drawable.navbar_documentation)
    )
}
