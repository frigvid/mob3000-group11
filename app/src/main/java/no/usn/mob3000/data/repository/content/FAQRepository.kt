package no.usn.mob3000.data.repository.content

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.content.FaqDto
import no.usn.mob3000.data.source.remote.docs.FAQDataSource
import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.domain.model.FaqUpdateData
import no.usn.mob3000.domain.repository.IFAQRepository

class FAQRepository(
    private val faqDataSource: FAQDataSource = FAQDataSource()
) : IFAQRepository {

    override suspend fun fetchFAQ(): Result<List<FAQData>> {
        return try {
            val faqDtoList = faqDataSource.fetchAllFAQ()
            Result.success(faqDtoList.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteFAQ(faqId: String): Result<Unit> {
        return try {
            faqDataSource.deleteFAQById(faqId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFAQ(faqId: String, updatedData: FaqUpdateData): Result<Unit> {
        val originalFAQ = faqDataSource.fetchFAQById(faqId)
        return if (originalFAQ != null) {
            val updatedFaqDto = FaqDto(
                faqId = faqId,
                createdAt = originalFAQ.createdAt,
                modifiedAt = Clock.System.now(),
                createdByUser = originalFAQ.createdByUser,
                title = updatedData.title,
                summary = updatedData.summary,
                content = updatedData.content,
                isPublished = updatedData.isPublished
            )
            faqDataSource.updateFAQ(faqId, updatedFaqDto, FaqDto.serializer())
        } else {
            Result.failure(Exception("Original FAQ data not found"))
        }
    }

    override suspend fun fetchFAQById(faqId: String): Result<FAQData?> {
        return try {
            val faqDto = faqDataSource.fetchFAQById(faqId)
            Result.success(faqDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertFAQ(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String?
    ): Result<Unit> {
        val currentUserId = userId ?: "00ba54a6-c585-4871-905e-7d53262f05c1"
        val faqItem = FaqDto(
            faqId = null,
            createdAt = Clock.System
                .now(),
            modifiedAt = Clock.System
                .now(),
            createdByUser = currentUserId,
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )
        return faqDataSource.insertFAQ(faqItem, FaqDto.serializer())
    }

    private fun FaqDto.toDomainModel(): FAQData {
        return FAQData(
            title = this.title ?: "",
            summary = this.summary ?: "",
            content = this.content ?: "",
            isPublished = this.isPublished ?: false,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser ?: "",
            faqId = this.faqId ?: ""
        )
    }
}
