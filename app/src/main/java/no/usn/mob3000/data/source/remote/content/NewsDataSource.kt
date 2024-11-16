package no.usn.mob3000.data.source.remote.content

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.remote.NewsDto
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
class NewsDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    /**
     * Fetches all rows from the news table
     *
     * @return A list of NewsDto objects representing the fetched rows.
     */
    suspend fun fetchAllNews(): List<NewsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("news")
            .select()
            .decodeList()
    }
    /**
     * Deletes a news by its ID.
     *
     * @param newsId The ID of the news to be deleted.
     */
    suspend fun deleteNewsById(newsId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("news")
            .delete { filter { eq("id", newsId) } }
    }
    /**
     * Fetches a news by its ID.
     *
     * @param newsId The ID of the news to be fetched.
     */
    suspend fun fetchNewsById(newsId: String): NewsDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("news")
            .select()
            {filter { eq("id", newsId) }}
            .decodeSingleOrNull()
    }
    /**
     * Updates an existing news by its ID with new data
     *
     * @param newsId The ID of the news to be updated.
     * @param updatedData The new data for the news.
     * @throws Exception If an error occurs during the update operation.
     */
    suspend fun updateNews(newsId: String, updatedData: NewsDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient
                .from("news")
                .update(updatedData) {
                    filter { eq("id", newsId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     * Inserts a new row into the news table
     *
     * @param newsItem The NewsDto object representing the new row to be inserted.
     * @return A result indicating the success or failure of the insertion operation.
     * @throws Exception If an error occurs during the insertion process.
     */
    suspend fun insertNews(newsItem: NewsDto): Result<Unit> {
        return try {
            supabaseClient.from("news").insert(newsItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
