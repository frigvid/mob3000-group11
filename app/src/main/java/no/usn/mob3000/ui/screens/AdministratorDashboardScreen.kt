package no.usn.mob3000.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * Administrator dashboard allowing administrators to promote/demote users, and delete them.
 *
 * TODO: Fetch user's isAdmin status and cache it in ViewModel state.
 *
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun AdministratorDashboardScreen() {
    var users by remember { mutableStateOf(dummyUsers) }

    Viewport { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.admin_dashboard_subtitle),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            UserList(
                users = users,
                onPromoteUser = { userId ->
                    users = users.map { user ->
                        if (user.id == userId) user.copy(isAdmin = true) else user
                    }
                },
                onDemoteUser = { userId ->
                    users = users.map { user ->
                        if (user.id == userId) user.copy(isAdmin = false) else user
                    }
                },
                onDeleteUser = { userId ->
                    users = users.filter { it.id != userId }
                }
            )
        }
    }
}

/**
 * Function that displays a list of users.
 *
 * @param users List of users to display.
 * @param onPromoteUser Callback function for when a user is promoted.
 * @param onDemoteUser Callback function for when a user is demoted.
 * @param onDeleteUser Callback function for when a user is deleted.
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun UserList(
    users: List<User>,
    onPromoteUser: (String) -> Unit,
    onDemoteUser: (String) -> Unit,
    onDeleteUser: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserListItem(
                user = user,
                onPromoteUser = onPromoteUser,
                onDemoteUser = onDemoteUser,
                onDeleteUser = onDeleteUser
            )
        }
    }
}

/**
 * Function that displays a single user item in the list.
 *
 * @param user User data to display.
 * @param onPromoteUser Callback function for when the user is promoted.
 * @param onDemoteUser Callback function for when the user is demoted.
 * @param onDeleteUser Callback function for when the user is deleted.
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun UserListItem(
    user: User,
    onPromoteUser: (String) -> Unit,
    onDemoteUser: (String) -> Unit,
    onDeleteUser: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (user.profilePicture != null) {
                Image(
                    painter = painterResource(user.profilePicture),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.user_icon_placeholder),
                    contentDescription = "Default profile picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = user.displayName ?: user.id,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = user.id,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        if (user.isAdmin) onDemoteUser(user.id)
                        else onPromoteUser(user.id)
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = DefaultButton)
                ) {
                    Icon(
                        painter = painterResource(
                            if (user.isAdmin) R.drawable.user_demote
                            else R.drawable.user_promote
                        ),
                        contentDescription =
                            if (user.isAdmin) "Demote administrator to regular user"
                            else "Promote regular user to administrator",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = { onDeleteUser(user.id) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.user_delete),
                        contentDescription = "Delete user.",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/* TODO: Extract to the data layer, and fix it when actually fetching users. */
data class User(
    val id: String,
    val displayName: String?,
    val profilePicture: Int?,
    val isAdmin: Boolean
)

/* TODO: Dummy data. Fetch users from the data layer instead. */
val dummyUsers = listOf(
    User("437f3972-ab77-415e-b95c-da5da50c0cc7", "Cheeseburderman", R.drawable.navbar_profile, true),
    User("00ba54a6-c585-4871-905e-7d53262f05c1", "RoboticOrangutan", null, false),
    User("a6cee00f-b510-43e3-8a2e-adab1dea1d95", null, null, false),
    User("72a759ef-1f8b-4592-906a-1fa7c9e78876", "Hinrik", null, false),
    User("2aa27782-a630-486f-9ac7-b9eaface4773", null, null, false),
    User("5fb35946-323b-41db-a605-f9e8daafd9ab", null, null, false)
)
