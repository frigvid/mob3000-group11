package no.usn.mob3000.domain.repository

import no.usn.mob3000.domain.model.DocsData

interface IDocsRepository {
    suspend fun fetchDocuments(): Result<List<DocsData>>
    suspend fun deleteDocs(docsId: String): Result<Unit>
}
