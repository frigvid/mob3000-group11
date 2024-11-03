package no.usn.mob3000.domain.repository

import no.usn.mob3000.domain.model.DocsData
import no.usn.mob3000.domain.model.DocsUpdateData

/**
 * Interface for document repository.
 *
 * @author 258030
 * @created 2024-10-30
 */
interface IDocsRepository {
    suspend fun fetchDocuments(): Result<List<DocsData>>
    suspend fun fetchDocsById(docsId: String): Result<DocsData?>
    suspend fun deleteDocs(docsId: String): Result<Unit>
    suspend fun updateDocs(docsId: String, updatedData: DocsUpdateData): Result<Unit>
    suspend fun insertDocs(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String?
    ): Result<Unit>
}
