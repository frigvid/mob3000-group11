package no.usn.mob3000.ui.screens.info.faq

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
import no.usn.mob3000.ui.components.base.Viewport
import no.usn.mob3000.ui.theme.DefaultButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import no.usn.mob3000.ui.theme.DefaultListItemBackground
import java.text.SimpleDateFormat
import java.util.*

/**
 * This is the FAQ page, where frequently asked questions will be displayed.
 *
 * @param faqList The list of FAQs to display.
 * @param onFAQClick Callback function to navigate to [CreateFAQScreen] with a [FAQ] object.
 * @param onCreateFAQClick Callback function to navigate to [CreateFAQScreen].
 * @param setFAQList ViewModel function to save a list of [FAQ] objects to state.
 * @param setSelectedFAQ ViewModel function to save a specific [FAQ] object to state.
 * @param clearSelectedFAQ ViewModel function to clear selected [FAQ] object.
 * @author frigvid
 * @created 2024-09-24
 */
@Composable
fun FAQScreen(
    faqList: List<FAQ>,
    onFAQClick: (FAQ) -> Unit,
    onCreateFAQClick: () -> Unit,
    setFAQList: (List<FAQ>) -> Unit,
    setSelectedFAQ: (FAQ) -> Unit,
    clearSelectedFAQ: () -> Unit
) {
    /* TODO: Replace dummy data with data fetched from back-end. */
    LaunchedEffect(Unit) {
        clearSelectedFAQ()

        val dummyFAQs = listOf(
            FAQ(
                "1",
                Date(),
                Date(),
                "User1",
                "How do I reset my password?",
                "Password reset process",
                "To reset your password, follow these steps:\n1. Go to the login page\n2. Click on 'Forgot Password'\n3. Enter your email address\n4. Check your email for reset instructions\n5. Follow the link in the email to create a new password",
                true
            ),
            FAQ(
                "2",
                Date(),
                Date(),
                "User2",
                "What payment methods do you accept?",
                "Accepted payment methods",
                "We accept the following payment methods:\n- Credit Cards (Visa, MasterCard, American Express)\n- PayPal\n- Bank Transfer\n- Apple Pay\n- Google Pay",
                true
            ),
            FAQ(
                "3",
                Date(),
                Date(),
                "User3",
                "How can I contact customer support?",
                "Customer support contact information",
                "You can contact our customer support team via:\n- Email: support@example.com\n- Phone: +1 (123) 456-7890\n- Live Chat: Available on our website 24/7\n- Social Media: @exampleSupport on Twitter",
                false
            )
        )

        setFAQList(dummyFAQs)
    }

    Viewport(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateFAQClick,
                containerColor = DefaultButton
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Create FAQ"
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
            items(faqList) { faq ->
                FAQItem(
                    faq = faq,
                    onClick = {
                        setSelectedFAQ(faq)
                        onFAQClick(faq)
                    }
                )
            }
        }
    }
}

/**
 * Composable function to display individual [FAQ] objects.
 *
 * @param faq The [FAQ] object.
 * @param onClick What happens when you click the [FAQ] UI object.
 * @author frigvid
 * @created 2024-10-12
 */
@Composable
fun FAQItem(
    faq: FAQ,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DefaultListItemBackground),
        border = if (faq.isPublished) null else BorderStroke(width = 2.dp, color = Color.Red)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = faq.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            faq.subtitle?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            faq.content?.let {
                Text(
                    text = it,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "${stringResource(R.string.faq_created_at)}: ${SimpleDateFormat(stringResource(R.string.faq_date_pattern), Locale.getDefault()).format(faq.createdAt)} | ${stringResource(R.string.faq_modified_at)}: ${SimpleDateFormat(stringResource(R.string.faq_date_pattern), Locale.getDefault()).format(faq.modifiedAt)}",
                fontSize = 12.sp
            )
        }
    }
}

/* TODO: Extract to data layer and fix so that it can be used with fetched data. */
data class FAQ(
    val id: String,
    val createdAt: Date,
    val modifiedAt: Date,
    val createdBy: String,
    val title: String,
    val subtitle: String?,
    val content: String?,
    val isPublished: Boolean
)
