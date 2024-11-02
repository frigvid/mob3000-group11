package no.usn.mob3000.data.source.remote.content

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import no.usn.mob3000.data.model.content.NewsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.domain.model.NewsData
import no.usn.mob3000.domain.model.NewsUpdateData

class NewsDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {

    suspend fun fetchAllNews(): List<NewsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("news")
            .select()
            .decodeList()
    }

    suspend fun deleteNewsById(newsId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("news")
            .delete { filter { eq("id", newsId) } }

    }

    suspend fun fetchNewsById(newsId: String): NewsDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("news")
            .select()
            {filter { eq("id", newsId) }}
            .decodeSingleOrNull()
    }

    suspend fun updateNews(newsId: String, updatedData: NewsDto, serializer: KSerializer<NewsDto>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonElement: JsonElement = Json.encodeToJsonElement(serializer, updatedData)
            supabaseClient
                .from("news")
                .update(jsonElement) {
                    filter { eq("id", newsId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertNews(newsItem: NewsDto, serializer: KSerializer<NewsDto>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonElement: JsonElement = Json.encodeToJsonElement(serializer, newsItem)
            supabaseClient.from("news").insert(jsonElement)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}


