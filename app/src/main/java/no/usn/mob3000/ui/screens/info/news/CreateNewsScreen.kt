package no.usn.mob3000.ui.screens.info.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton

/**
 * This is the equivalent to a blog-poster, where the admin can post news to the app. The news will be connected to the database
 * so it can be displayed for all users. The publish button is currently a dud until further notice.
 *
 * TODO: Make a {@code UPDATE} function for inserting data into the [NEWS] table.
 * TODO: Go over options for customizability (font size, colors, font types, images, etc.)
 *
 * @author 258030 (Eirik)
 * @Created 2024-10-10
 */
@Composable
fun CreateNewsScreen() {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Viewport { innerPadding ->
        Scaffold(
            modifier = Modifier.padding(innerPadding),
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                title = ""
                                subtitle = ""
                                description = ""
                            },
                            colors = ButtonDefaults.buttonColors(DefaultButton),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.group_create_reset_all))
                        }
                        Button(
                            onClick = { /* TODO: Save news in database to be auto-generated on NewsScreen */ },
                            colors = ButtonDefaults.buttonColors(DefaultButton),
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        ) {
                            Text(stringResource(R.string.publish))
                        }
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.group_create_prompt_title)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text(stringResource(R.string.subtitle)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.group_create_prompt_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 15
                )

            }
        }
    }
}
