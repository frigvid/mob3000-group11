/**
 * These are document, news and faq related data needed for the UI layer.
 *
 * TODO: Abstraction, especially for "UpateData"
 *
 * @author 258030
 * @created 2024-10-30
 */


package no.usn.mob3000.domain.model

import kotlinx.datetime.Instant

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

data class DocsUpdateData(
    val title: String,
    val summary: String,
    val content: String,
    val isPublished: Boolean
)

