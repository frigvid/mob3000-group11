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
import androidx.compose.material3.Icon;
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.colorResource
import no.usn.mob3000.LocalNavController
import no.usn.mob3000.Viewport

/**
 * The profile screen.
 *
 * @author frigvid, Hussein
 * @created 2024-09-12
 */
@Composable
fun ProfileScreen(
    onProfileEditClick: () -> Unit,
    onProfileAddFriendsClick: () -> Unit,
    onProfileFriendRequestsClick: () -> Unit
) {
    val navController = LocalNavController.current
    Viewport(
        topBarActions = {
            IconButton(onClick = onProfileEditClick) {
                Icon(
                    painter = painterResource(R.drawable.profile_edit),
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
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
            ProfileHeader()
            ProfileStats()
            AboutSection()
            FriendsSection()
        }
    }
}


@Composable
fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(colorResource(id = R.color.beige)),
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
        Text(
            text = "Example user",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ProfileStats() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id=R.color.soft_beige))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem("ELO", "430")
        StatItem("Games", "0")
        StatItem("Wins", "0")
        StatItem("Losses", "0")
        StatItem("Draws", "0")
    }
}
@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun AboutSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "About Me",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Example user description.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
fun FriendsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Friends",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
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
                text = "Example user name",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
