package no.usn.mob3000.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.usn.mob3000.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon;
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.ui.theme.ProfileUserBackground
import no.usn.mob3000.ui.theme.ProfileUserStatisticsBackground

/**
 * The profile screen.
 *
 * This screen show the user's profile picture, their display name, statistics about the user,
 * a section regarding them, and their friends (if any).
 *
 * @param onProfileEditClick Callback function to navigate to [ProfileEditScreen].
 * @param onProfileAddFriendsClick Callback function to navigate to [ProfileAddFriendsScreen].
 * @param onProfileFriendRequestsClick Callback function to navigate to [ProfileFriendRequestsScreen].
 * @author frigvid, Hussein
 * @contributor 258030
 * @created 2024-09-12
 */
@Composable
fun ProfileScreen(
    onProfileEditClick: (UserProfile) -> Unit,
    onProfileAddFriendsClick: () -> Unit,
    onProfileFriendRequestsClick: () -> Unit,
    fetchFriends: () -> Unit,
    fetchUser: (String) -> Unit,
    fetchUserById: (String) -> Unit,
    friendState: StateFlow<Result<List<FriendData>>>,
    userIdState: StateFlow<String?>,
    userProfilesMap: StateFlow<Map<String, UserProfile>>,
    setSelectedUser: (UserProfile) -> Unit
) {
    val friendResult by friendState.collectAsState()
    val userId by userIdState.collectAsState()
    val userProfiles by userProfilesMap.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { id ->
            fetchUser(id)
            fetchFriends()
        }
    }

    Viewport(
        topBarActions = {
            userProfiles[userId]?.let { user ->
                IconButton(onClick = {
                    setSelectedUser(user)
                    onProfileEditClick(user)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.profile_edit),
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }
            IconButton(onClick = onProfileAddFriendsClick) {
                Icon(
                    painter = painterResource(R.drawable.profile_add_friends),
                    contentDescription = "Add Friend",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
            IconButton(onClick = onProfileFriendRequestsClick) {
                Icon(
                    painter = painterResource(R.drawable.profile_pending_friends),
                    contentDescription = "Friend Requests",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProfileHeader(userResult = Result.success(userProfiles[userId]))
            ProfileStats(userProfile = userProfiles[userId])
            AboutSection()
            FriendsSection(
                friendResult = friendResult,
                userId = userId,
                fetchUserById = fetchUserById,
                userProfilesMap = userProfiles
            )
        }
    }
}




/**
 * Composable function that displays the header section of the profile screen.
 *
 * This section includes the user's profile picture and display name.
 *
 * TODO: Input actual data about the user here, such as their profile picture and display name.
 *
 * @author Hussein
 * @contributor frigvid
 * @created 2024-10-11
 */
@Composable
fun ProfileHeader(userResult: Result<UserProfile?>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(ProfileUserBackground),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.profile_icon),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        )
        userResult.onSuccess { user ->
            user?.let {
                Text(
                    text = it.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } ?: Text(
                text = "No user data",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }.onFailure {
            Text(
                text = "Failed to load user profile.",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

/**
 * Composable function that displays the statistics section of the profile screen.
 *
 * This section shows various user statistics such as ELO rating, games played,
 * wins, losses, draws, and country.
 *
 * TODO: INPUT CORRECT DATATABLE HERE, WRONG DATA REFERENCE IN DOMAIN
 *
 * @author Hussein
 * @contributor frigvid, 258030
 * @created 2024-10-11
 */
@Composable
fun ProfileStats(userProfile: UserProfile?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ProfileUserStatisticsBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(stringResource(R.string.profile_stat_elo), userProfile?.eloRank?.toString() ?: "N/A")
        StatItem(stringResource(R.string.profile_stat_games), "0")
        StatItem(stringResource(R.string.profile_stat_wins), "0")
        StatItem(stringResource(R.string.profile_stat_losses), "0")
        StatItem(stringResource(R.string.profile_stat_draws), "0")
        StatItem(stringResource(R.string.profile_stat_country), userProfile?.nationality ?: "N/A")
    }
}

/**
 * Composable function that displays a single statistic item.
 *
 * @param label String The label for the statistic.
 * @param value String The value of the statistic.
 * @author Hussein
 * @created 2024-10-11
 */
@Composable
fun StatItem(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

/**
 * Composable function that displays the "About Me" section of the profile screen.
 *
 * This section shows a brief description or biography of the user.
 *
 * @author Hussein
 * @created 2024-10-11
 */
@Composable
fun AboutSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.profile_about_me),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        /* TODO: Fetch the user's description from cached state in ViewModel. */
        Text(
            text = "Example user description.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Composable function that displays the friends section of the profile screen.
 *
 * This section lists the user's friends, showing their profile pictures and names.
 *
 * @author Hussein
 * @created 2024-10-11
 */
@Composable
fun friendComponent(
    friendResult: Result<List<FriendData>>,
    userId: String?,
    fetchUserById: (String) -> Unit,
    userProfilesMap: Map<String, UserProfile>
) {
    LazyColumn {
        friendResult.onSuccess { friends ->
            items(friends) { friend ->
                val friendIdToDisplay = if (userId != friend.user1) friend.user1 else friend.user2
                LaunchedEffect(friendIdToDisplay) {
                    if (!userProfilesMap.containsKey(friendIdToDisplay)) {
                        fetchUserById(friendIdToDisplay)
                    }
                }

                val friendDisplayName = userProfilesMap[friendIdToDisplay]?.displayName ?: friendIdToDisplay

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {  }
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_icon),
                        contentDescription = "Friend Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )

                    Text(
                        text = friendDisplayName,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }.onFailure {
            item {
                Text(
                    text = "Failed to load friends",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun FriendsSection(
    friendResult: Result<List<FriendData>>,
    userId: String?,
    fetchUserById: (String) -> Unit,
    userProfilesMap: Map<String, UserProfile>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.profile_friends),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        friendComponent(
            friendResult = friendResult,
            userId = userId,
            fetchUserById = fetchUserById,
            userProfilesMap = userProfilesMap
        )
    }
}


