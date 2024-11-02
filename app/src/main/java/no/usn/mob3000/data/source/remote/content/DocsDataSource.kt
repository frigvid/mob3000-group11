package no.usn.mob3000.data.source.remote.docs

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

class DocsDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {

    suspend fun fetchAllDocs(): List<DocsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("docs")
            .select()
            .decodeList()
    }

    suspend fun deleteDocsById(docsId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("docs")
            .delete { filter { eq("id", docsId) } }

    }

    suspend fun fetchDocsById(docsId: String): DocsDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("docs")
            .select()
            { filter { eq("id", docsId) } }
            .decodeSingleOrNull()
    }

    suspend fun updateDocs(
        docsId: String,
        updatedData: DocsDto,
        serializer: KSerializer<DocsDto>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonElement: JsonElement = Json.encodeToJsonElement(serializer, updatedData)
            supabaseClient
                .from("docs")
                .update(jsonElement) {
                    filter { eq("id", docsId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertDocs(docsItem: DocsDto, serializer: KSerializer<DocsDto>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonElement: JsonElement = Json.encodeToJsonElement(serializer, docsItem)
            supabaseClient.from("docs").insert(jsonElement)
            Result.success(Unit)
            } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
