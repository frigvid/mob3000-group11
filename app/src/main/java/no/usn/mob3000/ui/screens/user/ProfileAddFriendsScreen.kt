package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Screen for allowing users to search for other users and sending them a friend request.
 *
 * @author frigvid
 * @contributors 258030
 * @created 2024-10-11
 */
@Composable
fun ProfileAddFriendsScreen(
    selectedUser: UserProfile? = null,
    fetchNonFriends: () -> Unit,
    nonFriendState: StateFlow<Result<List<UserProfile>>>,
    sendFriendRequest: () -> Unit,
    onUserClick: (String) -> Unit,
    fetchUser: (String) -> Unit,
    fetchUserById: (String) -> Unit,
    friendState: StateFlow<Result<List<FriendData>>>,
    userIdState: StateFlow<String?>,
    userProfilesMap: StateFlow<Map<String, UserProfile>>,
    setSelectedUser: (UserProfile) -> Unit
) {
    val nonFriends by nonFriendState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fetchNonFriends()
    }

    val filteredNonFriends = nonFriends.getOrNull()?.filter {
        it.displayName.contains(searchQuery, ignoreCase = true) || it.userId.contains(searchQuery)
    } ?: emptyList()

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(stringResource(R.string.profile_add_friends_search_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredNonFriends) { user ->
                    UserListItem(
                        user = user,
                        onClick = {
                            onUserClick(user.userId)
                            sendFriendRequest() }
                    )
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    user: UserProfile,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.profile_icon),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
        )

        Text(
            text = user.displayName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ) {
            Text(stringResource(R.string.profile_add_friends_add_button))
        }
    }
}
