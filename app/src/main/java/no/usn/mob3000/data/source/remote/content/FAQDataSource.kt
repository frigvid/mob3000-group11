package no.usn.mob3000.data.source.remote.docs

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.remote.FaqDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * Data source class responsible for the database communication. All database
 * interactions are executed on the IO dispatcher to ensure optimal performance for
 * network-bound operations.
 *
 * @param supabaseClient The Supabase client for making API requests.
 * @author 258030
 * @created 2024-10-30
 */
class FAQDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    /**
     * Fetches all rows from the faq table
     *
     * @return A list of FaqDto objects representing the fetched rows.
     */
    suspend fun fetchAllFAQ(): List<FaqDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("faq")
            .select()
            .decodeList()
    }
    /**
     * Deletes a FAQ by its ID.
     *
     * @param faqId The ID of the FAQ to be deleted.
     */
    suspend fun deleteFAQById(faqId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("faq")
            .delete { filter { eq("id", faqId) } }
    }
    /**
     * Fetches a FAQ by its ID.
     *
     * @param faqId The ID of the FAQ to be fetched.
     */
    suspend fun fetchFAQById(faqId: String): FaqDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("faq")
            .select()
            { filter { eq("id", faqId) } }
            .decodeSingleOrNull()
    }
    /**
     * Updates an existing FAQ by its ID with new data.
     *
     * @param faqId The ID of the FAQ to be updated.
     * @param updatedData The new data for the FAQ.
     * @throws Exception If an error occurs during the update operation.
     */
    suspend fun updateFAQ(faqId: String, updatedData: FaqDto): Result<Unit> = withContext(Dispatchers.IO) {
            try {
                supabaseClient
                    .from("faq")
                    .update(updatedData) {
                        filter { eq("id", faqId) }
                    }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    /**
     * Inserts a new row into the faq table.
     *
     * @param faqItem The FaqDto object representing the new row to be inserted.
     * @return A result indicating the success or failure of the insertion operation.
     * @throws Exception If an error occurs during the insertion process.
     */
    suspend fun insertFAQ(faqItem: FaqDto): Result<Unit> = withContext(Dispatchers.IO) {
       try {
            supabaseClient.from("faq").insert(faqItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
