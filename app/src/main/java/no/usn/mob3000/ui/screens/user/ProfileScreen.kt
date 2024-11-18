package no.usn.mob3000.ui.screens.user

import androidx.compose.material3.Button
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import no.usn.mob3000.domain.model.auth.UserGameStats
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.viewmodel.socials.ProfileViewModel
import no.usn.mob3000.ui.theme.DefaultButton
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
 * @author frigvid, Husseinabdulameer11
 * @contributor 258030
 * @created 2024-09-12
 */
@Composable
fun ProfileScreen(
    onProfileEditClick: (UserProfile) -> Unit,
    onProfileAddFriendsClick: (UserProfile) -> Unit,
    onProfileFriendRequestsClick: () -> Unit,
    fetchFriends: () -> Unit,
    fetchUser: (String) -> Unit,
    fetchUserById: (String) -> Unit,
    friendState: StateFlow<Result<List<FriendData>>>,
    userIdState: StateFlow<String?>,
    userProfilesMap: StateFlow<Map<String, UserProfile>>,
    setSelectedUser: (UserProfile) -> Unit,
    authenticationState: StateFlow<AuthenticationState>,
    authenticationStateUpdate: () -> Unit,
    onLoginClick: () -> Unit,
    userGameStats: StateFlow<Result<UserGameStats>>,
    viewModel: ProfileViewModel
) {
    val friendResult by friendState.collectAsState()
    val userId by userIdState.collectAsState()
    val userProfiles by userProfilesMap.collectAsState()
    val state by remember { authenticationState }.collectAsState()
    val currentUserProfile by viewModel.currentUserProfile.collectAsState()
    val gameStats by userGameStats.collectAsState()

    LaunchedEffect(userId) {
        authenticationStateUpdate()
        userId?.let { id ->
            fetchFriends()
        }
    }
    Viewport(
        topBarActions = {
            currentUserProfile?.let { user ->
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

                IconButton(onClick = {
                    setSelectedUser(user)
                    onProfileAddFriendsClick(user)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.profile_add_friends),
                        contentDescription = "Add Friend",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
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
            when (state) {
                is AuthenticationState.Error,
                is AuthenticationState.Unauthenticated -> {
                    Button(
                        onClick = onLoginClick,
                        colors = ButtonDefaults.buttonColors(DefaultButton),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) { Text(stringResource(R.string.settings_section_user_button_login)) }
                }
                is AuthenticationState.Authenticated -> {
                    ProfileHeader(userResult = Result.success(currentUserProfile))
                    ProfileStats(userProfile = currentUserProfile, gameStats = gameStats)
                    AboutSection()
                    FriendsSection(
                        friendResult = friendResult,
                        userProfilesMap = userProfiles,
                        userId = userId,
                        fetchUserById = fetchUserById
                    )
                }
                else -> return@Viewport
            }
        }
    }
}

/**
 * Composable function that displays the header section of the profile screen.
 *
 * This section includes the user's profile picture and display name.
 *
 *
 * @author Husseinabdulameer11
 * @contributor frigvid, 258030
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
 * @author Husseinabdulameer11
 * @contributor frigvid, 258030
 * @created 2024-10-11
 */
@Composable
fun ProfileStats(userProfile: UserProfile?, gameStats: Result<UserGameStats>) {
    val userGameStats = gameStats.getOrNull()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ProfileUserStatisticsBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(stringResource(R.string.profile_stat_elo), userProfile?.eloRank?.toString() ?: "N/A")
        StatItem(stringResource(R.string.profile_stat_games), (userGameStats?.wins?.plus(userGameStats.losses)
            ?.plus(userGameStats.draws)).toString())
        StatItem(stringResource(R.string.profile_stat_wins), userGameStats?.wins?.toString() ?: "0")
        StatItem(stringResource(R.string.profile_stat_losses), userGameStats?.losses?.toString() ?: "0")
        StatItem(stringResource(R.string.profile_stat_draws), userGameStats?.draws?.toString() ?: "0")
        StatItem(stringResource(R.string.profile_stat_country), userProfile?.nationality ?: "N/A")
    }
}

/**
 * Composable function that displays a single statistic item.
 *
 * @param label String The label for the statistic.
 * @param value String The value of the statistic.
 * @author Husseinabdulameer11
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
 * @author Husseinabdulameer11
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
 * @author Husseinabdulameer11
 * @created 2024-10-11
 */
@Composable
fun FriendComponent(
    friendResult: Result<List<FriendData>>,
    userProfilesMap: Map<String, UserProfile>,
    userId: String?,
    fetchUserById: (String) -> Unit
) {
    LazyColumn {
        friendResult.onSuccess { friends ->
            items(friends) { friend ->
                val friendIdToDisplay = if (friend.user1 != userId) friend.user1 else friend.user2
                LaunchedEffect(friendIdToDisplay) {
                    if (!userProfilesMap.containsKey(friendIdToDisplay)) {
                        fetchUserById(friendIdToDisplay)
                    }
                }
                val friendDisplayName = userProfilesMap[friendIdToDisplay]?.displayName ?: "No name"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { }
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

/**
 * @author Husseinabdulameer11
 * @created 2024-10-11
 */
@Composable
fun FriendsSection(
    friendResult: Result<List<FriendData>>,
    userProfilesMap: Map<String, UserProfile>,
    userId: String?,
    fetchUserById: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.profile_friends),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FriendComponent(
            friendResult = friendResult,
            userProfilesMap = userProfilesMap,
            userId = userId,
            fetchUserById = fetchUserById
        )
    }
}
