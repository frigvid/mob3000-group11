package no.usn.mob3000.ui.screens.info.docs

import android.util.Log
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
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.ui.theme.DefaultListItemBackground
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
    documentations: List<DocsDto>,
    onDocumentationClick: (DocsDto) -> Unit,
    onCreateDocumentationClick: () -> Unit,
    setDocumentationList: (List<DocsDto>) -> Unit,
    setSelectedDocumentation: (DocsDto) -> Unit,
    clearSelectedDocumentation: () -> Unit
) {
    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        clearSelectedDocumentation()
        val dbUtilities = DbUtilities()

        try {
            val result = dbUtilities.fetchItems("docs", DocsDto.serializer())
            result.onSuccess { items ->
                setDocumentationList(items)
            }
        } catch (e: Exception) {
            Log.e("News", "Error fetching documentation", e)
        }
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateDocumentationClick,
                containerColor = DefaultButton
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create Documentation")
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
            items(documentations) { docsItem ->
                DocsItem(
                    documentations = docsItem,
                    onClick = {
                        setSelectedDocumentation(docsItem)
                        onDocumentationClick(docsItem)
                    }
                )
            }
        }
    }
}

/**
 * Composable function to display individual news items.
 */
@Composable
fun DocsItem(
    documentations: DocsDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = documentations.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            documentations.summary?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
