package no.usn.mob3000.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import no.usn.mob3000.R
import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.admin.AdminDataSource
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.model.auth.User
import no.usn.mob3000.domain.usecase.admin.DeleteUserUseCase
import no.usn.mob3000.domain.usecase.admin.DemoteToRegularUserUseCase
import no.usn.mob3000.domain.usecase.admin.FetchAllUsersUseCase
import no.usn.mob3000.domain.usecase.admin.PromoteToAdminUseCase
import no.usn.mob3000.ui.components.DangerousActionDialogue
import no.usn.mob3000.ui.components.base.Viewport

/**
 * Administrator dashboard allowing administrators to promote/demote users, and delete them.
 *
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun AdministratorDashboardScreen(
    promoteToAdminUseCase: PromoteToAdminUseCase = PromoteToAdminUseCase(AdminDataSource()),
    demoteToRegularUserUseCase: DemoteToRegularUserUseCase = DemoteToRegularUserUseCase(AdminDataSource()),
    deleteUserUseCase: DeleteUserUseCase = DeleteUserUseCase(AdminDataSource()),
    fetchAllUsersUseCase: FetchAllUsersUseCase =
        FetchAllUsersUseCase(
            AdminDataSource(),
            AuthRepository(AuthDataSource(), UserDataSource())
        )
) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    val expandedUserIds = remember { mutableStateMapOf<String, Boolean>() }
    val coroutineScope = rememberCoroutineScope()

    var showPromoteDialog by remember { mutableStateOf(false) }
    var showDemoteDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val result = fetchAllUsersUseCase()

        result.onSuccess { fetchedUsers ->
            users = fetchedUsers
        }.onFailure { error ->
            Logger.e("Something went wrong while fetching all users!", throwable = error)
        }
    }

    if (showPromoteDialog && selectedUserId != null) {
        DangerousActionDialogue(
            title = stringResource(R.string.admin_dashboard_dialog_promote_user_title),
            onConfirm = {
                coroutineScope.launch {
                    promoteToAdminUseCase(selectedUserId!!)
                    users = users.map { u ->
                        if (u.id == selectedUserId) u.copy(isAdmin = true) else u
                    }
                    showPromoteDialog = false
                    selectedUserId = null
                }
            },
            onDismiss = {
                showPromoteDialog = false
                selectedUserId = null
            }
        )
    }

    if (showDemoteDialog && selectedUserId != null) {
        DangerousActionDialogue(
            title = stringResource(R.string.admin_dashboard_dialog_demote_user_title),
            onConfirm = {
                coroutineScope.launch {
                    demoteToRegularUserUseCase(selectedUserId!!)
                    users = users.map { u ->
                        if (u.id == selectedUserId) u.copy(isAdmin = false) else u
                    }
                    showDemoteDialog = false
                    selectedUserId = null
                }
            },
            onDismiss = {
                showDemoteDialog = false
                selectedUserId = null
            }
        )
    }

    if (showDeleteDialog && selectedUserId != null) {
        DangerousActionDialogue(
            title = stringResource(R.string.admin_dashboard_dialog_delete_user_title),
            onConfirm = {
                coroutineScope.launch {
                    deleteUserUseCase(selectedUserId!!)
                    users = users.filter { it.id != selectedUserId }
                    showDeleteDialog = false
                    selectedUserId = null
                }
            },
            onDismiss = {
                showDeleteDialog = false
                selectedUserId = null
            }
        )
    }

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

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users) { user ->
                    UserListItem(
                        user = user,
                        isExpanded = expandedUserIds[user.id] ?: false,
                        onExpandedChange = { isExpanded ->
                            expandedUserIds[user.id] = isExpanded
                        },
                        onPromoteUser = { userId ->
                            selectedUserId = userId
                            showPromoteDialog = true
                        },
                        onDemoteUser = { userId ->
                            selectedUserId = userId
                            showDemoteDialog = true
                        },
                        onDeleteUser = { userId ->
                            selectedUserId = userId
                            showDeleteDialog = true
                        }
                    )
                }
            }
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
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onPromoteUser: (String) -> Unit,
    onDemoteUser: (String) -> Unit,
    onDeleteUser: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onExpandedChange(!isExpanded) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_icon_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = user.email ?: stringResource(R.string.admin_dashboard_fragment_email_unknown),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = user.id,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text =
                        if (user.isAdmin) {
                            stringResource(R.string.admin_dashboard_fragment_is_administrator)
                        } else {
                            stringResource(R.string.admin_dashboard_fragment_is_regular_user)
                        },
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector =
                    if (isExpanded) {
                        Icons.Filled.KeyboardArrowUp
                    } else {
                        Icons.Filled.KeyboardArrowDown
                    },
                contentDescription = null
            )
        }
        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (user.isAdmin) {
                    Button(
                        onClick = { onDemoteUser(user.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary,)
                    ) {
                        Text(stringResource(R.string.admin_dashboard_fragment_button_demote))
                    }
                } else {
                    Button(
                        onClick = { onPromoteUser(user.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary,)
                    ) {
                        Text(stringResource(R.string.admin_dashboard_fragment_button_promote))
                    }
                }

                Button(
                    onClick = { onDeleteUser(user.id) },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(stringResource(R.string.admin_dashboard_fragment_button_delete))
                }
            }
        }
    }
}
