package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.socials.friendrequests.FriendRequestItem

/**
 * Screen to allow users to see pending friend requests for their acccounts,
 * and accept or reject them.
 *
 * TODO: Add data layer functionality that actually fetches friend requests in the background.
 * TODO: Cache known, but unhandled, friend requests for the user in ViewModel state.
 *
 * @author Hussein, frigvid
 * @created 2024-10-11
 */
@Composable
fun ProfileFriendRequestsScreen() {
    Viewport { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            /* TODO: If-else block to check if cached list is empty, wait for it to update first.
             *       Show buffering if waiting. If background fetching, that'll hopefully not be
             *       necessary though.
             */
          FriendRequestItem()
        }
    }
}
