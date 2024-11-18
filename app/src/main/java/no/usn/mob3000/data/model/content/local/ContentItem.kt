package no.usn.mob3000.data.model.content.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * Data class representing the local tables for news items.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Entity(tableName = "news_table")
class NewsItemLocal(
    @PrimaryKey(autoGenerate = false) var newsId: String,
    @ColumnInfo(name = "created_at") var createdAt: Instant,
    @ColumnInfo(name = "modified_at") var modifiedAt: Instant,
    @ColumnInfo(name = "created_by") var createdByUser: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "summary") var summary: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "is_published") var isPublished: Boolean
)

/**
 * Data class representing the local tables for docs items.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Entity(tableName = "docs_table")
class DocsItemLocal (
    @PrimaryKey(autoGenerate = false) var docsId: String,
    @ColumnInfo(name = "created_at") var createdAt: Instant,
    @ColumnInfo(name = "modified_at") var modifiedAt: Instant,
    @ColumnInfo(name = "created_by") var createdByUser: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "summary") var summary: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "is_published") var isPublished: Boolean
)

/**
 * Data class representing the local tables for faq items.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Entity(tableName = "faq_table")
class FaqItemLocal(
    @PrimaryKey(autoGenerate = false) var faqId: String,
    @ColumnInfo(name = "created_at") var createdAt: Instant,
    @ColumnInfo(name = "modified_at") var modifiedAt: Instant,
    @ColumnInfo(name = "created_by") var createdByUser: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "summary") var summary: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "is_published") var isPublished: Boolean
)
