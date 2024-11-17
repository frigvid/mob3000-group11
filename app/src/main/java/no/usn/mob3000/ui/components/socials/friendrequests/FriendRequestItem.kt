package no.usn.mob3000.ui.components.socials.friendrequests

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.info.ConfirmationDialog

import no.usn.mob3000.ui.theme.DefaultButton
@Composable
fun FriendRequestItem(

){
    val showDialog = remember { mutableStateOf(false) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Image(
            painter = painterResource(id =R.drawable.user_icon_placeholder),
            contentDescription = "Profile icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier =Modifier.width((16.dp)))
        Column(
            modifier = Modifier.weight(1f)
        ){
            Text(text = "Request from: User")
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {},
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ){
            Text(text = "Accept",color=Color.White)
        }
        Button(
            onClick = { showDialog.value = true },
            modifier = Modifier.padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(DefaultButton)
        ){
            Text(text = "Decline",color=Color.White)
        }
    }
    if (showDialog.value) {
        ConfirmationDialog(
            showDialog = showDialog,
            onConfirm = {
                // Handle the confirm action (e.g., decline the friend request)
                // ...

                // Close the dialog
                showDialog.value = false
            },
            onDismiss = {
                // Close the dialog when dismissed
                showDialog.value = false
            }
        )
    }
}
