package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.social.FriendRequestItem

/**
 * This screen allows users to view pending friend requests, and provides options to accept or decline
 * each request. It fetches friend request data and displays them in a vertical list.
 *
 * @param fetchFriendRequests Function to fetch the friend requests.
 * @param friendRequestState StateFlow containing the result of fetching friend requests.
 * @param userProfilesMap Map of user profiles.
 * @param fetchUserById Function to fetch a user by their ID.
 * @param onAcceptFriendRequest Function to handle accepting a friend request.
 * @param onDeclineFriendRequest Function to handle declining a friend request.
 * @author Husseinabdulameer11, frigvid, 258030
 * @created 2024-10-11
 */
@Composable
fun ProfileFriendRequestsScreen(
    fetchFriendRequests: () -> Unit,
    friendRequestState: StateFlow<Result<List<FriendRequestData>>>,
    userProfilesMap: StateFlow<Map<String, UserProfile>>,
    fetchUserById: (String) -> Unit,
    onAcceptFriendRequest: (String) -> Unit,
    onDeclineFriendRequest: (String) -> Unit
) {
    val friendRequests by friendRequestState.collectAsState()
    val userProfiles by userProfilesMap.collectAsState()

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
                        Text(
                            text = stringResource(R.string.profile_pending_friend_requests_no_requests),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        requests.forEach { friendRequest ->
                            FriendRequestItem(
                                friendRequest = friendRequest,
                                userProfilesMap = userProfiles,
                                fetchUserById = fetchUserById,
                                onAccept = { onAcceptFriendRequest(friendRequest.friendRequestId) },
                                onDecline = { onDeclineFriendRequest(friendRequest.friendRequestId) }
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.profile_pending_friend_requests_error_fetching),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
