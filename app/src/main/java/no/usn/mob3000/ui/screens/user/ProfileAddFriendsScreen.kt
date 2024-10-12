package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.usn.mob3000.Viewport

/**
 * Screen for allowing users to search for other users and sending them a friend request.
 *
 * @author Hussein
 * @created 2024-10-11
 */
@Composable
fun ProfileAddFriendsScreen() {
    Viewport { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) { Text("Add friends.") }
    }
}
