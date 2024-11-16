package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

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
fun ProfileFriendRequestsScreen(
    fetchFriendRequests: () -> Unit,
    friendRequestState: StateFlow<Result<List<FriendRequestData>>>,
    onAcceptFriendRequest: (String) -> Unit,
    onDeclineFriendRequest: (String) -> Unit
) {
    val friendRequests by friendRequestState.collectAsState()

    LaunchedEffect(Unit) {
        fetchFriendRequests()
    }

    Viewport { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (friendRequests.isSuccess) {
                    val requests = friendRequests.getOrNull() ?: emptyList()
                    if (requests.isEmpty()) {
                        Text(text = "No new friend requests", color = Color.Gray)
                    } else {
                        requests.forEach { friendRequest ->
                            friendRequest.displayName?.let {
                                FriendRequestItem(
                                    displayName = it,
                                    onAccept = { onAcceptFriendRequest(friendRequest.friendRequestId) },
                                    onDecline = { onDeclineFriendRequest(friendRequest.friendRequestId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FriendRequestItem(
    displayName: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon_placeholder),
            contentDescription = "Profile icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = displayName)
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onAccept,
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ) {
            Text(text = "Accept", color = Color.White)
        }
        Button(
            onClick = onDecline,
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ) {
            Text(text = "Decline", color = Color.White)
        }
    }
}
