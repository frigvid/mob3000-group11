package no.usn.mob3000.data.source.remote.docs

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import no.usn.mob3000.data.model.content.FaqDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * Data source class responsible for managing FAQ-related operations.
 *
 * @param supabaseClient The Supabase client for making API requests.
 * @author 258030
 * @created 2024-10-30
 */
class FAQDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    /**
     * Fetches a list of all FAQ.
     */
    suspend fun fetchAllFAQ(): List<FaqDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("faq")
            .select()
            .decodeList()
    }

    /**
     * Deletes a FAQ by its ID.
     */
    suspend fun deleteFAQById(faqId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("faq")
            .delete { filter { eq("id", faqId) } }
    }

    /**
     * Fetches a FAQ by its ID.
     */
    suspend fun fetchFAQById(faqId: String): FaqDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("faq")
            .select()
            { filter { eq("id", faqId) } }
            .decodeSingleOrNull()
    }

    /**
     * Updates a chosen FAQ by its ID.
     */
    suspend fun updateFAQ(faqId: String, updatedData: FaqDto, serializer: KSerializer<FaqDto>): Result<Unit> =
        withContext(Dispatchers.IO) {
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
     * Inserts a new FAQ.
     */
    suspend fun insertFAQ(faqItem: FaqDto, serializer: KSerializer<FaqDto>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.from("faq").insert(faqItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
