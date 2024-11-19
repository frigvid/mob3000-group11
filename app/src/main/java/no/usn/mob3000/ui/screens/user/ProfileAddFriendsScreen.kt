package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.ui.components.base.Viewport

/**
 * Screen for allowing users to search for other users and sending them a friend request.
 *
 * @param fetchNonFriends Callback function to get all users who are not friends with the user.
 * @param nonFriendState State for whether or not the users are friends.
 * @param sendFriendRequest Callback function to send a friend request.
 * @param onUserClick Callback function to insert a friend request into the database.
 * @author frigvid
 * @contributors 258030, Husseinabdulameer11
 * @created 2024-10-11
 */
@Composable
fun ProfileAddFriendsScreen(
    fetchNonFriends: () -> Unit,
    nonFriendState: StateFlow<Result<List<UserProfile>>>,
    sendFriendRequest: () -> Unit,
    onUserClick: (String) -> Unit
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
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
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
                            sendFriendRequest()
                        }
                    )
                }
            }
        }
    }
}

/**
 * @param user The user's [UserProfile].
 * @param onClick Arbitrary unit.
 */
@Composable
private fun UserListItem(
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
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.profile_add_friends_add_button))
        }
    }
}
