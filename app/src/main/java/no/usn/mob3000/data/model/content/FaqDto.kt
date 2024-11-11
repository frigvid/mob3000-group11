package no.usn.mob3000.data.model.content

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the `public.faq` table.
 *
 * @param faqId The FAQ item's UUID.
 * @param createdAt A timestamp representing when the FAQ item was created.
 * @param modifiedAt A timestamp representing when the FAQ item was modified.
 * @param createdByUser A UUID reference to a matching user in `auth.users`.
 * @param title The title of the FAQ item.
 * @param summary The summary of the FAQ item.
 * @param content The content of the FAQ item.
 * @param isPublished A boolean switch representing whether or not the FAQ item is published.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class FaqDto(
    @SerialName("id")
    val faqId: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("modified_at")
    val modifiedAt: Instant,
    @SerialName("created_by")
    val createdByUser: String? = null,
    @SerialName("title")
    val title: String? = "",
    @SerialName("summary")
    val summary: String? = "",
    @SerialName("content")
    val content: String? = "",
    @SerialName("is_published")
    val isPublished: Boolean = false
)
