package no.usn.mob3000.data.repository.content

import kotlinx.datetime.Clock
import no.usn.mob3000.data.source.remote.docs.DocsDataSource
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.model.content.DocsUpdateData
import no.usn.mob3000.domain.repository.IDocsRepository

/**
 * Repository class responsible for managing document-related operations.
 *
 * @param docsDataSource The data source for document-related operations.
 * @author 258030
 * @contributor frigvid
 * @created 2024-10-30
 */
class DocsRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val docsDataSource: DocsDataSource = DocsDataSource()
) : IDocsRepository {
    /**
     * Fetches a list of all documents.
     */
    override suspend fun fetchDocuments(): Result<List<DocsData>> {
        return try {
            val docsDtoList = docsDataSource.fetchAllDocs()
            Result.success(docsDtoList.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes a document by its ID.
     */
    override suspend fun deleteDocs(docsId: String): Result<Unit> {
        return try {
            docsDataSource.deleteDocsById(docsId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates a chosen document by its ID.
     */
    override suspend fun updateDocs(docsId: String, updatedData: DocsUpdateData): Result<Unit> {
        val originalDocs = docsDataSource.fetchDocsById(docsId)
        if (originalDocs != null) {
            val updatedDocsDto = DocsDto(
                docId = docsId,
                createdAt = originalDocs.createdAt,
                modifiedAt = Clock.System.now(),
                createdByUser = originalDocs.createdByUser,
                title = updatedData.title,
                summary = updatedData.summary,
                content = updatedData.content,
                isPublished = updatedData.isPublished
            )
            return docsDataSource.updateDocs(docsId, updatedDocsDto, DocsDto.serializer())
        } else {
            return Result.failure(Exception("Original docs data not found"))
        }
    }
    /**
     * Fetches a document by its ID.
     */
    override suspend fun fetchDocsById(docsId: String): Result<DocsData?> {
        return try {
            val docsDto = docsDataSource.fetchDocsById(docsId)
            Result.success(docsDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inserts a new document.
     */
    override suspend fun insertDocs(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String?
    ): Result<Unit> {
        val docsItem = DocsDto(
            docId = null,
            createdAt = Clock.System
                .now(),
            modifiedAt = Clock.System
                .now(),
            createdByUser = authDataSource.getCurrentUserId(),
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return docsDataSource.insertDocs(docsItem, DocsDto.serializer())

    }

    /**
     * Maps a DocsDto to a DocsData. For usage in the domain layer.
     */
    private fun DocsDto.toDomainModel(): DocsData {
    return DocsData(
        title = this.title ?: "",
        summary = this.summary ?: "",
        content = this.content ?: "",
        isPublished = this.isPublished ?: false,
        createdAt = this.createdAt,
        modifiedAt = this.modifiedAt,
        createdByUser = this.createdByUser ?: "",
        docsId = this.docId ?: ""
    )
}

}
