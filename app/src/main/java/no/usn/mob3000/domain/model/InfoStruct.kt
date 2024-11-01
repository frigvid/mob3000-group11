package no.usn.mob3000.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

data class NewsData(
    val newsId: String,
    val createdAt: Instant,
    val modifiedAt: Instant,
    val createdByUser: String,
    val title: String,
    val summary: String,
    val content: String,
    val isPublished: Boolean
)

data class FAQData(
    val faqId: String,
    val createdAt: Instant,
    val modifiedAt: Instant,
    val createdByUser: String,
    val title: String,
    val summary: String,
    val content: String,
    val isPublished: Boolean
)

data class DocsData(
    val docsId: String,
    val createdAt: Instant,
    val modifiedAt: Instant,
    val createdByUser: String,
    val title: String,
    val summary: String,
    val content: String,
    val isPublished: Boolean
)

data class NewsUpdateData(
    val title: String,
    val summary: String,
    val content: String,
    val isPublished: Boolean
)

data class FaqUpdateData(
    val title: String,
    val summary: String,
    val content: String,
    val isPublished: Boolean
)

