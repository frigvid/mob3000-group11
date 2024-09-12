package no.usn.mob3000.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            /* TODO: Implement Train action */
        }) {
            Text("Train")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            /* TODO: Implement Play action */
        }) {
            Text("Play")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            /* TODO: Implement History action */
        }) {
            Text("History")
        }
    }
}
