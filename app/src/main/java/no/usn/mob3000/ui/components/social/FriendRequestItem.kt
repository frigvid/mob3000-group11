package no.usn.mob3000.ui.components.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.ui.theme.DefaultButton

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
 * @author Husseinabdulameer11
 * @created 2024-11-16
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

        Spacer( modifier = Modifier.width(8.dp))

        Button(
            onClick = onAccept,
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ) {
            Text(
                text = stringResource(R.string.profile_pending_friend_requests_accept_button),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Button(
            onClick = { showDialog.value = true},
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ) {
            Text(
                text = stringResource(R.string.profile_pending_friend_requests_decline_button),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    if (showDialog.value) {
         ProfileConfirmDialog(
            showDialog = showDialog,
            onConfirm = {
                onDecline()
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false },
            stringResource(R.string.profile_component_friend_request_title),
            stringResource(R.string.profile_component_friend_request_text),
            stringResource(R.string.profile_component_friend_request_confirm),
            stringResource(R.string.profile_component_friend_request_dismiss)
        )
    }
}
