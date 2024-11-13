package no.usn.mob3000.data.source.remote.docs

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.remote.DocsDto
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
class DocsDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    /**
     * Fetches all rows from the docs table
     *
     * @return A list of DocsDto objects representing the fetched rows.
     */
    suspend fun fetchAllDocs(): List<DocsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("docs")
            .select()
            .decodeList()
    }

    /**
     * Deletes a document by its ID.
     *
     * @param docsId The ID of the document to be deleted..
     */
    suspend fun deleteDocsById(docsId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("docs")
            .delete { filter { eq("id", docsId) } }
    }

    /**
     * Fetches a document by its ID.
     *
     * @param docsId The ID of the document to be fetched.
     */
    suspend fun fetchDocsById(docsId: String): DocsDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("docs")
            .select()
            { filter { eq("id", docsId) } }
            .decodeSingleOrNull()
    }

    /**
     * Updates an existing document by its ID with new data
     *
     * @param docsId The ID of the document to be updated.
     * @param updatedData The new data for the document.
     * @throws Exception If an error occurs during the update operation.
     */
    suspend fun updateDocs(docsId: String, updatedData: DocsDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient
                .from("docs")
                .update(updatedData) {
                    filter { eq("id", docsId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inserts a new row into the docs table
     *
     * @param docsItem The DocsDto object representing the new row to be inserted.
     * @return A result indicating the success or failure of the insertion operation.
     * @throws Exception If an error occurs during the insertion process.
     */
    suspend fun insertDocs(docsItem: DocsDto): Result<Unit> {
        return try {
            supabaseClient.from("docs").insert(docsItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
