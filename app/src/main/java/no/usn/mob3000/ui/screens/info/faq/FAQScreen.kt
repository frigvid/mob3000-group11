package no.usn.mob3000.ui.screens.info.faq

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.usn.mob3000.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.usn.mob3000.data.model.content.FaqDto
import no.usn.mob3000.data.repository.content.DbUtilities
import no.usn.mob3000.ui.theme.DefaultListItemBackground
import no.usn.mob3000.ui.screens.info.news.NewsScreen


/**
 * Screen for the documentation page.
 *
 * TODO: This, [FAQScreen] and [NewsScreen] can probably be made more generic, so they depend
 *       on fellow components similar to the web-version.
 *
 *
 * @author frigvid, 258030 (Eirik)
 * @created 2024-09-23
 */
@Composable
fun FAQScreen(
    faqList: List<FaqDto>,
    onFAQClick: (FaqDto) -> Unit,
    onCreateFAQClick: () -> Unit,
    setFAQList: (List<FaqDto>) -> Unit,
    setSelectedFAQ: (FaqDto) -> Unit,
    clearSelectedFAQ: () -> Unit
) {
    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        clearSelectedFAQ()
        val dbUtilities = DbUtilities()

        try {
            val result = dbUtilities.fetchItems("faq", FaqDto.serializer())
            result.onSuccess { items ->
                setFAQList(items)
            }
        } catch (e: Exception) {
            Log.e("FAQ", "Error fetching FAQ", e)
        }
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateFAQClick,
                containerColor = DefaultButton
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create FAQ")
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
            items(faqList) { faqItem ->
                FaqItem(
                    faqList = faqItem,
                    onClick = {
                        setSelectedFAQ(faqItem)
                        onFAQClick(faqItem)
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
fun FaqItem(
    faqList: FaqDto,
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
                text = faqList.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            faqList.summary?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
