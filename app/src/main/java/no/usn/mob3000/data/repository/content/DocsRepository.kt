package no.usn.mob3000.data.repository.content

import no.usn.mob3000.data.source.remote.docs.DocsDataSource
import no.usn.mob3000.domain.model.DocsData
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.domain.repository.IDocsRepository

class DocsRepository(
    private val docsDataSource: DocsDataSource = DocsDataSource()
) : IDocsRepository {

    override suspend fun fetchDocuments(): Result<List<DocsData>> {
        return try {
            val docsDtoList = docsDataSource.fetchAllDocs()
            Result.success(docsDtoList.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDocs(docsId: String): Result<Unit> {
        return try {
            docsDataSource.deleteDocsById(docsId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


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
