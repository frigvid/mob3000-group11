package no.usn.mob3000.domain.usecase.content

import no.usn.mob3000.domain.repository.IDocsRepository

class InsertDocsUseCase(
    private val docsRepository: IDocsRepository
) {
    suspend fun execute(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String? = null
    ): Result<Unit> {
        return docsRepository.insertDocs(title, summary, content, isPublished, userId)
    }
}
