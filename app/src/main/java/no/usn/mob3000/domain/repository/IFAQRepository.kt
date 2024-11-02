package no.usn.mob3000.domain.repository

import no.usn.mob3000.domain.model.FAQData
import no.usn.mob3000.domain.model.FaqUpdateData

interface IFAQRepository {
    suspend fun fetchFAQ(): Result<List<FAQData>>
    suspend fun fetchFAQById(faqId: String): Result<FAQData?>
    suspend fun deleteFAQ(faqId: String): Result<Unit>
    suspend fun updateFAQ(faqId: String, updatedData: FaqUpdateData): Result<Unit>
    suspend fun insertFAQ(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean,
        userId: String?
    ): Result<Unit>
}
