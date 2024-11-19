package no.usn.mob3000.data.repository.content.remote

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.content.local.DocsItemLocal
import no.usn.mob3000.data.model.content.remote.DocsDto
import no.usn.mob3000.data.repository.content.local.DocsRepositoryLocal
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.content.DocsDataSource
import no.usn.mob3000.domain.model.content.DocsData
import no.usn.mob3000.domain.repository.content.IDocsRepository
import java.util.UUID

/**
 * Repository class responsible for managing operations related to the docs table. It uses [DocsDataSource] for fetching and handling
 * database actions. Via [IDocsRepository] it makes a possible communication route with the UI domain layer, without the domain layer getting accidental access
 * to parts of the code it never was suppose to have. It also helps the application run smoother, as there are less dependencies between layers.
 *
 * @param authDataSource The data source for authentication-related operations.
 * @param docsDataSource The data source for document-related operations.
 * @param docsRepositoryLocal The local data source for document-related operations.
 * @author 258030
 * @contributor frigvid
 * @created 2024-10-30
 */
class DocsRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val docsDataSource: DocsDataSource = DocsDataSource(),
    private val docsRepositoryLocal: DocsRepositoryLocal
) : IDocsRepository {
    /**
     * Fetches a list of all documents to later be used for generating document-cards in the UI. It maps the fetched data to a domain model, so it can be used in the UI.
     *
     * @return A result containing a list of documents.
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun fetchAllDocsLocal(): Result<List<DocsData>> {
        return try {
            val localDocs = docsRepositoryLocal.fetchAllDocs()
            Result.success(localDocs.map { it.toDomainModel() })
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Fetches a list of all documents to later be used for generating document-cards in the UI.
     * It maps the fetched data to a domain model, so it can be used in the UI.
     *
     * @return A result containing a list of documents.
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun refreshDocsFromNetwork(): Result<Unit> {
        return try {
            val networkDocsList = docsDataSource.fetchAllDocs()
            val localDocsList = networkDocsList.map { it.toLocalModel() }
            docsRepositoryLocal.clearAllDocs()
            docsRepositoryLocal.insertDocsList(localDocsList)
            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Deletes a document by its ID. The ID is directly fetched by what specific card has been opened from one of the main screens
     * [DocumentationScreen] -> [DocumentationDetailsScreen].
     *
     * @return A result indicating the success or failure of the deletion operation.
     * @throws Exception If an error occurs during the deletion process.
     */
    override suspend fun deleteDocs(docsId: String): Result<Unit> {
        return try {
            docsDataSource.deleteDocsById(docsId)
            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Updates a chosen document by its ID. The ID is directly fetched by what specific card has been opened from one of the main screens
     * [DocumentationScreen] -> [DocumentationDetailsScreen]. As long as the docsId exist, the update instance starts. Without an actual UUID for the doc
     * the update operation would fail, but to prevent unnecessary API-calls we use a validator here. There is a throw in [DocsDataSource],
     * but the validator also make us aware if the method for fetching the ID fails.
     *
     * @return A result indicating the success or failure of the update operation.
     */
    override suspend fun updateDocs(
        docsId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val originalDocs = docsDataSource.fetchDocsById(docsId)

        if (originalDocs != null) {
            val updatedDocsDto = DocsDto(
                docId = docsId,
                createdAt = originalDocs.createdAt,
                modifiedAt = Clock.System.now(),
                createdByUser = originalDocs.createdByUser,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )

            return docsDataSource.updateDocs(docsId, updatedDocsDto)
        } else {
            return Result.failure(Exception("Original docs data not found"))
        }
    }

    /**
     * Fetches a document by its ID. Used for populating the update screen and identify what row we are working with.
     *
     * @return A result containing the fetched document.
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun fetchDocsById(docsId: String): Result<DocsData?> {
        return try {
            val docsDto = docsDataSource.fetchDocsById(docsId)
            Result.success(docsDto?.toDomainModel())
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Inserts a new row into the database. The database has an auto generated UUID for new rows, but we call it here for good measure. Not passing the value seems
     * to interfere with the null exception. It displays the time of creation using [kotlinx.datetime.Clock] and tracks what user made the row.
     *
     * @return A result indicating the success or failure of the insertion operation.
     * @throws Exception If an error occurs during the insertion process.
     */
    override suspend fun insertDocs(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val docsItem = DocsDto(
            docId = UUID.randomUUID().toString(),
            createdAt = Clock.System.now(),
            modifiedAt = Clock.System.now(),
            createdByUser = authDataSource.getCurrentUserId(),
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )

        return docsDataSource.insertDocs(docsItem)
    }

    /**
     * Maps a DocsDto to a DocsData. For usage in the domain layer. Might be moved if repository are made abstract.
     */
    private fun DocsDto.toDomainModel(): DocsData {
        return DocsData(
            title = this.title ?: "",
            summary = this.summary ?: "",
            content = this.content ?: "",
            isPublished = this.isPublished,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser ?: "",
            docsId = this.docId
        )
    }

    /**
     * Maps a documentation DTO to a local model.
     */
    private fun DocsDto.toLocalModel(): DocsItemLocal {
        return DocsItemLocal(
            docsId = this.docId,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser ?: "",
            title = this.title ?: "",
            summary = this.summary ?: "",
            content = this.content ?: "",
            isPublished = this.isPublished
        )

    }

    /**
     * Maps a local documentation model to a domain model.
     */
    private fun DocsItemLocal.toDomainModel(): DocsData {
        return DocsData(
            docsId = this.docsId,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser,
            title = this.title,
            summary = this.summary,
            content = this.content,
            isPublished = this.isPublished
        )
    }
}
