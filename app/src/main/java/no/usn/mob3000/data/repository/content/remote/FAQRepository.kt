package no.usn.mob3000.data.repository.content.remote

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.content.local.FaqItemLocal
import no.usn.mob3000.data.model.content.remote.FaqDto
import no.usn.mob3000.data.repository.content.local.FAQRepositoryLocal
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.content.FAQDataSource
import no.usn.mob3000.domain.model.content.FAQData
import no.usn.mob3000.domain.repository.content.IFAQRepository
import java.util.UUID

/**
 * Repository class responsible for managing operations related to the docs table. It uses [FAQDataSource] for fetching and handling
 * database actions. Via [IFAQRepository] it makes a possible communication route with the UI domain layer, without the domain layer getting accidental access
 * to parts of the code it never was suppose to have. It also helps the application run smoother, as there are less dependencies between layers.
 *
 * @param authDataSource The data source for authentication-related operations.
 * @param faqDataSource The data source for FAQ-related operations.
 * @param faqRepositoryLocal The local FAQ repository.
 * @author 258030
 * @contributor frigvid
 * @created 2024-10-30
 */
class FAQRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val faqDataSource: FAQDataSource = FAQDataSource(),
    private val faqRepositoryLocal: FAQRepositoryLocal
) : IFAQRepository {
    /**
     * Fetches a list of all FAQs to later be used for generating FAQ-cards in the UI. It maps the fetched data to a domain model, so it can be used in the UI.
     *
     * @return A result containing a list of FAQs.
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun fetchAllFaqLocal(): Result<List<FAQData>> {
        return try {
            val localFAQ = faqRepositoryLocal.fetchAllFaq()
            Result.success(localFAQ.map { it.toDomainModel() })
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Fetches a list of all FAQs to later be used for generating FAQ-cards in the UI.
     * It maps the fetched data to a domain model, so it can be used in the UI.
     *
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun refreshFaqFromNetwork(): Result<Unit> {
        return try {
            val networkFaqList = faqDataSource.fetchAllFAQ()
            val localFaqList = networkFaqList.map { it.toLocalModel() }

            faqRepositoryLocal.clearAllFaq()
            faqRepositoryLocal.insertFaqList(localFaqList)

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Deletes a FAQ by its ID. The ID is directly fetched by what specific card has been opened from one of the main screens.
     * [FAQScreen] -> [FAQDetailsScreen]
     *
     * @return A result indicating the success or failure of the deletion operation.
     * @throws Exception If an error occurs during the deletion process.
     */
    override suspend fun deleteFAQ(faqId: String): Result<Unit> {
        return try {
            faqDataSource.deleteFAQById(faqId)
            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Updates a chosen FAQ by its ID. The ID is directly fetched by what specific card has been opened from one of the main screens
         * [FAQScreen] -> [FAQDetailsScreen]. As long as the faqId exist, the update instance starts. Without an actual UUID for the faq
     * the update operation would fail, but to prevent unnecessary API-calls we use a validator here. There is a throw in [FAQDataSource],
     * but the validator also make us aware if the method for fetching the ID fails.
     *
     * @return A result indicating the success or failure of the update operation.
     * */
    override suspend fun updateFAQ(
        faqId: String,
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val originalFAQ = faqDataSource.fetchFAQById(faqId)

        return if (originalFAQ != null) {
            val updatedFaqDto = FaqDto(
                faqId = faqId,
                createdAt = originalFAQ.createdAt,
                modifiedAt = Clock.System.now(),
                createdByUser = originalFAQ.createdByUser,
                title = title,
                summary = summary,
                content = content,
                isPublished = isPublished
            )

            faqDataSource.updateFAQ(faqId, updatedFaqDto)
        } else {
            Result.failure(Exception("Original FAQ data not found"))
        }
    }

    /**
     * Fetches a FAQ by its ID. Used for populating the update screen and identify what row we are working with.
     *
     * @return A result containing the fetched FAQ.
     * @throws Exception If an error occurs during the fetching process.
     */
    override suspend fun fetchFAQById(faqId: String): Result<FAQData?> {
        return try {
            val faqDto = faqDataSource.fetchFAQById(faqId)

            Result.success(faqDto?.toDomainModel())
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
    override suspend fun insertFAQ(
        title: String,
        summary: String,
        content: String,
        isPublished: Boolean
    ): Result<Unit> {
        val faqItem = FaqDto(
            faqId = UUID.randomUUID().toString(),
            createdAt = Clock.System.now(),
            modifiedAt = Clock.System.now(),
            createdByUser = authDataSource.getCurrentUserId(),
            title = title,
            summary = summary,
            content = content,
            isPublished = isPublished
        )

        return faqDataSource.insertFAQ(faqItem)
    }

    /**
     * Maps a FaqDto to a FAQData. For usage in the domain layer. Might be moved if repository are made abstract.
     */
    private fun FaqDto.toDomainModel(): FAQData {
        return FAQData(
            title = this.title ?: "",
            summary = this.summary ?: "",
            content = this.content ?: "",
            isPublished = this.isPublished,
            createdAt = this.createdAt,
            modifiedAt = this.modifiedAt,
            createdByUser = this.createdByUser ?: "",
            faqId = this.faqId
        )
    }

    /**
     * Maps an FAQ DTO to a local model.
     */
    private fun FaqDto.toLocalModel(): FaqItemLocal {
        return FaqItemLocal(
            faqId = this.faqId,
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
     * Maps a local FAQ model to a domain model.
     */
    private fun FaqItemLocal.toDomainModel(): FAQData {
        return FAQData(
            faqId = this.faqId,
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
