package no.usn.mob3000.data.model.content

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the `public.news` table.
 *
 * @param newsId The news item's UUID.
 * @param createdAt A timestamp representing when the news item was created.
 * @param modifiedAt A timestamp representing when the news item was modified.
 * @param createdByUser A UUID reference to a matching user in `auth.users`.
 * @param title The title of the news item.
 * @param summary The summary of the news item.
 * @param content The content of the news item.
 * @param isPublished A boolean switch representing whether or not the news item is published.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class NewsDto(
    @SerialName("id")
    val newsId: String? = null,
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
