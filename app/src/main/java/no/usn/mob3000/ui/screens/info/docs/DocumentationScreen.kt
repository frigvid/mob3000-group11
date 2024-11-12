package no.usn.mob3000.ui.screens.info.docs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.usn.mob3000.R
import no.usn.mob3000.Viewport
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.usn.mob3000.ui.screens.info.faq.FAQScreen
import no.usn.mob3000.ui.screens.info.news.NewsScreen
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for the documentation page.
 *
 * TODO: This, [FAQScreen] and [NewsScreen] can probably be made more generic, so they depend
 *       on fellow components similar to the web-version.
 *
 * @param documentations The list of documentation stored in the ViewModel's state.
 * @param onDocumentationClick Callback function to navigate to the [Documentation] object's [DocumentationDetailsScreen].
 * @param onCreateDocumentationClick Callback function to navigate to [CreateDocumentationScreen].
 * @param setDocumentationList ViewModel function to store the list of [Documentation] objects in state.
 * @param setSelectedDocumentation ViewModel function to store a specific [Documentation] object in state.
 * @param clearSelectedDocumentation ViewModel function to clear the stored state documentation object.
 * @author frigvid, 258030 (Eirik)
 * @created 2024-09-23
 */
@Composable
fun DocumentationScreen(
    documentations: List<Documentation>,
    onDocumentationClick: (Documentation) -> Unit,
    onCreateDocumentationClick: () -> Unit,
    setDocumentationList: (List<Documentation>) -> Unit,
    setSelectedDocumentation: (Documentation) -> Unit,
    clearSelectedDocumentation: () -> Unit
) {
    /* TODO: Replace dummy data with data fetched from back-end. */
    LaunchedEffect(Unit) {
        clearSelectedDocumentation()

        val dummyDocumentations = listOf(
            Documentation(
                "1",
                Date(),
                Date(),
                "User1",
                "Getting Started",
                "A guide for beginners",
                "Content here",
                true
            ),
            Documentation(
                "2",
                Date(),
                Date(),
                "User2",
                "Advanced Techniques",
                "For experienced users",
                "More content",
                false
            ),
            Documentation(
                "3",
                Date(),
                Date(),
                "User3",
                "Troubleshooting",
                "Common issues and solutions",
                "Even more content",
                true
            )
        )

        setDocumentationList(dummyDocumentations)
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateDocumentationClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.shadow(15.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Create Documentation"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(documentations) { documentation ->
                DocumentationItem(
                    documentation = documentation,
                    onClick = {
                        setSelectedDocumentation(documentation)
                        onDocumentationClick(documentation)
                    }
                )
            }
        }
    }
}

/**
 * Composable function to display individual documentation items.
 *
 * @param documentation The [Documentation] object.
 * @param onClick Callback function to navigate to the [Documentation] object's [DocumentationDetailsScreen].
 * @author frigvid, 258030 (Eirik)
 * @created 2024-10-12
 */
@Composable
fun DocumentationItem(
    documentation: Documentation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        border =
            if (documentation.isPublished) null
            else BorderStroke(width = 2.dp, color = Color(0xFFFF0000))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = documentation.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            documentation.summary?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = stringResource(R.string.documentation_date_prefix) + ": ${SimpleDateFormat(stringResource(R.string.documentation_date_pattern), Locale.getDefault()).format(documentation.createdAt)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

/* TODO: Extract to data layer and fix for use with fetched data. */
data class Documentation(
    val id: String,
    val createdAt: Date,
    val modifiedAt: Date,
    val createdBy: String,
    val title: String,
    val summary: String?,
    val content: String?,
    val isPublished: Boolean
)
