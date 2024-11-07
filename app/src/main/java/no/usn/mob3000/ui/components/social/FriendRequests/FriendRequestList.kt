package no.usn.mob3000.ui.components.social.FriendRequests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.domain.model.Friend
import no.usn.mob3000.domain.model.FriendRequest

@Composable
fun FriendRequestList(
    friendRequests: List<FriendRequestsDto>,
    onAccept : (FriendRequestsDto) -> Unit,
    onDecline : (FriendRequestsDto) -> Unit
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        friendRequests.forEach { friendRequests ->
            FriendRequestItem(
                 friendRequest = friendRequests
        )
        }
    }
}
