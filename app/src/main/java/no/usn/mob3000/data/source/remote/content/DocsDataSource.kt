package no.usn.mob3000.data.source.remote.docs

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
}
