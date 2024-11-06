package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.mob3000.R
import no.usn.mob3000.ui.components.base.Viewport
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen to display full details about some documentation.
 *
 * @param selectedDocumentation The [Documentation] object to display.
 * @param onEditClick Callback function to navigate to [CreateDocumentationScreen].
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun DocumentationDetailsScreen(
    selectedDocumentation: Documentation?,
    onEditClick: () -> Unit
) {
    Viewport(
        topBarActions = {
            IconButton(onClick = onEditClick) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Documentation"
                )
            }
        }
    ) { innerPadding ->
        if (selectedDocumentation != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = selectedDocumentation.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    selectedDocumentation.summary?.let {
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    selectedDocumentation.content?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0xFFEFEBE9))
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.documentation_details_created_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.documentation_details_created_date_pattern), Locale.getDefault()).format(selectedDocumentation.createdAt)}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = stringResource(R.string.documentation_details_modified_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.documentation_details_modified_date_pattern), Locale.getDefault()).format(selectedDocumentation.modifiedAt)}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    /* TODO: Only display this for admins. */
                    Text(
                        text = stringResource(R.string.documentation_details_status_prefix) + "Status: ${if (selectedDocumentation.isPublished) stringResource(R.string.documentation_details_status_published) else stringResource(R.string.documentation_details_status_draft)}",
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            Text(stringResource(R.string.documentation_details_not_found))
        }
    }
}
