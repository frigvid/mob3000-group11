package no.usn.mob3000

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import no.usn.mob3000.ui.theme.*;
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Main activity file.
 *
 * @author Husseinabdulameer11, frigvid
 * @created 2024-09-02
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

// Main composable for handling the bottom bar and navigation.
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavigationGraph(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

// Sealed class to define bottom navigation items
sealed class BottomNavItem(val route: String, val icon: Int, val title: String) {
    data object Documents : BottomNavItem("Documentation", R.drawable.navbar_documentation, "Documentation")
    data object Profile : BottomNavItem("profile", R.drawable.navbar_user, "Profile")
    data object Home : BottomNavItem("home", R.drawable.navbar_home, "Home")
    data object Settings : BottomNavItem("settings", R.drawable.navbar_settings, "Settings")
    data object Notifications : BottomNavItem("notifications", R.drawable.navbar_news, "News")
}

// Bottom navigation bar composable
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Documents,
        BottomNavItem.Notifications,
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings,
    )

    BottomNavigation(
        backgroundColor = menu_bar_color,
        modifier = Modifier.height(70.dp)
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(bottom = if (isSelected) 2.dp else 4.dp),
                        tint = if (currentRoute == item.route) background_color else text_color.copy(alpha = 0.5f),
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = text_color,
                        style = Typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false,
            )
        }
    }
}

// Navigation graph
@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
        composable(BottomNavItem.Documents.route) {
            DocumentPage()
        }

        composable(BottomNavItem.Profile.route) {
            ProfilePage()
        }

        composable(BottomNavItem.Home.route) {
            HomePage()
        }

        composable(BottomNavItem.Settings.route) {
            SettingsPage()
        }

        composable(BottomNavItem.Notifications.route) {
            NotificationPage()
        }
    }
}

// Helper function to get the current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    return navBackStackEntry?.destination?.route
}

// Individual pages
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ColorfulButton(
                        color = Color(0xFF3b82f6),
                        icon = R.drawable.home_train,
                        text = stringResource(R.string.home_train_title)
                    )

                    ColorfulButton(
                        color = Color(0xFF22c55e),
                        icon = R.drawable.home_play,
                        text = stringResource(R.string.home_play_title)
                    )

                    ColorfulButton(
                        color = Color(0xFFef4444),
                        icon = R.drawable.home_history,
                        text = stringResource(R.string.home_history_title)
                    )
                }
            }
        }
    )
}

@Composable
fun ColorfulButton(color: Color, icon: Int, text: String) {
    Surface(
        modifier = Modifier
            .size(width = 150.dp, height = 150.dp)
            .clickable { /* Handle click action */ },
        color = color,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentPage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.documentation_title)) })
        },
        content = {}
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.profile_title)) })
        },
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsPage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        },
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationPage() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.news_title)) })
        },
        content = {}
    )
}
