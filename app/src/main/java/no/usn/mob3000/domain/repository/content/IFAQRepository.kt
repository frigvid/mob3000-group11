package no.usn.mob3000.domain.repository.content

import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.domain.model.content.FaqUpdateData

/**
 * Interface for FAQ repository.
 *
 * @author 258030
 * @created 2024-10-30
 */
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
