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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.components.info.ConfirmationDialog
import no.usn.mob3000.ui.components.socials.friendrequests.profileConfirmDialog
import no.usn.mob3000.ui.theme.DefaultButton

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
 * @author Hussein, frigvid, 258030
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
                        Text(text = "No new friend requests", color = Color.Gray)
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
                    Text(text = "Error fetching friend requests", color = Color.Red)
                }
            }
        }
    }
}
/**
 * Function that displays a single friend request item in the list.
 *
 * This item shows the user who sent the friend request, along with "Accept" and "Decline" buttons
 * to allow the user to respond to the request.
 *
 * @param friendRequest The [FriendRequestData] object representing the friend request.
 * @param userProfilesMap A map of user profiles keyed by user IDs, used to display the sender's information.
 * @param fetchUserById Function to fetch the profile of the user who sent the friend request, if their profile is not already available.
 * @param onAccept Function to handle the acceptance of the friend request.
 * @param onDecline Function to handle the decline of the friend request.
 */
@Composable
fun FriendRequestItem(
    friendRequest: FriendRequestData,
    userProfilesMap: Map<String, UserProfile>,
    fetchUserById: (String) -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val friendIdToDisplay = friendRequest.byUser
    val showDialog = remember { mutableStateOf(false) }
    LaunchedEffect(friendIdToDisplay) {
        if (!userProfilesMap.containsKey(friendIdToDisplay)) {
            fetchUserById(friendIdToDisplay)
        }
    }
    val displayName = userProfilesMap[friendIdToDisplay]?.displayName ?: stringResource(R.string.profile_pending_friend_requests_unknown_user)
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
            Text(text = stringResource(R.string.profile_pending_friend_requests_accept_button), color = Color.White)
        }
        Button(
            onClick ={ showDialog.value = true},
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ) {
            Text(text = stringResource(R.string.profile_pending_friend_requests_decline_button), color = Color.White)
        }
    }
    if (showDialog.value) {
        profileConfirmDialog(
            showDialog = showDialog,
            onConfirm = {
                onDecline()
                showDialog.value = false
            },
            onDismiss = {
                // Close the dialog when dismissed
                showDialog.value = false
            },
            "dismiss friend request?",
            "are you sure you want to dismiss this friend request?",
            "Delete",
            "Cancel"
        )
    }
}


