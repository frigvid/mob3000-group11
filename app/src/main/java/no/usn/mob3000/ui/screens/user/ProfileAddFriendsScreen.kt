package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.Image
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
import no.usn.mob3000.R
import no.usn.mob3000.Viewport

/**
 * Screen for allowing users to search for other users and sending them a friend request.
 *
 * @author frigvid
 * @created 2024-10-11
 */
@Composable
fun ProfileAddFriendsScreen() {
    var searchQuery by remember { mutableStateOf("") }

    /* TODO: Extract user fetcher to data layer. */
    val dummyUsers = remember {
        listOf(
            User("00ba54a6-c585-4871-905e-7d53262f05c1", "ChessMaster42", R.drawable.profile_icon),
            User("b82bcc28-79b4-4a5f-8efd-080a9c00dc2f", "PawnStar", R.drawable.profile_icon),
            User("3a574a5d-9406-4245-aebb-6074fc56b9ec", "KnightRider", R.drawable.profile_icon),
            User("b39a02b1-9b72-4134-905c-fa538b2a9a53", "BishopBoss", R.drawable.profile_icon),
            User("6237de51-a463-42c8-8c0a-5f0c8930ccc3", "RookiePlayer", R.drawable.profile_icon)
        )
    }

    val filteredUsers = dummyUsers.filter {
        it.username.contains(searchQuery, ignoreCase = true) || it.id.contains(searchQuery)
    }

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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { items(filteredUsers) { user -> UserListItem(user) } }
        }
    }
}

/**
 * Composable list item to display the user's profile picture, display name and UUID.
 *
 * @param user The (dummy) user data object.
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun UserListItem(
    user: User
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(user.profilePicture),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = user.username,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user.id,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Button(
            onClick = { /* TODO: Implement send friend request logic */ },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(start = 8.dp)
        ) { Text(stringResource(R.string.profile_add_friends_add_button)) }
    }
}

/* TODO: Extract to data layer and fix dummy User data class. */
data class User(
    val id: String,
    val username: String,
    val profilePicture: Int
)
