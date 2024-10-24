package no.usn.mob3000.data

import android.content.Context
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import no.usn.mob3000.MainActivity

/**
 * Singleton object responsible for managing the Supabase client instance.
 *
 * This wrapper provides centralized access to the Supabase client throughout the application.
 * It handles the initialization of the client with secure credentials and ensures that only one
 * instance of the client is created and used across the app.
 *
 * @author frigvid
 * @created 2024-10-07
 */
object SupabaseClientWrapper {
    private lateinit var supabaseClient: SupabaseClient

    /**
     * Initializes the Supabase client with secure credentials.
     *
     * This function should be called once at application startup, typically in the Application class
     * like [MainActivity]. It retrieves the Supabase URL and anonymous key from the SecureEnvManager
     * and uses them to create and configure the Supabase client.
     *
     * @param context The application context used to access secure environment variables.
     * @throws IllegalStateException if the Supabase credentials are not found in SecureEnvManager.
     * @author frigvid
     * @created 2024-10-07
     */
    fun initialize(context: Context) {
        val supabaseUrl = SecureEnvManager.getDecryptedValue(context, "NEXT_PUBLIC_SUPABASE_URL")
        val supabaseKey = SecureEnvManager.getDecryptedValue(context, "NEXT_PUBLIC_SUPABASE_ANON_KEY")

        if (supabaseUrl == null || supabaseKey == null) {
            throw IllegalStateException("Supabase credentials not found. Make sure to call SecureEnvManager.initializeEnvVariables(context) first.")
        }

        supabaseClient = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            // TODO: Investigate if more plugins are necessary.
            install(Auth)
            install(Postgrest)
        }
    }

    /**
     * Retrieves the initialized Supabase client instance.
     *
     * This function provides access to the Supabase client for performing database operations,
     * authentication, and other Supabase-related tasks throughout the application.
     *
     * @return The initialized SupabaseClient instance.
     * @throws IllegalStateException if the client hasn't been initialized before calling this function.
     * @author frigvid
     * @created 2024-10-07
     */
    fun getClient(): SupabaseClient {
        if (!::supabaseClient.isInitialized) {
            throw IllegalStateException("SupabaseClient not initialized. Call initialize(context) first.")
        }

        return supabaseClient
    }

    /**
     * Inserts a new news article into the database.
     *
     *  TODO: Abstract this so it can be used for all other insert functions.
     */
    suspend fun insertNews(title: String, summary: String?, content: String?, isPublished: Boolean): Result<Unit> {
        return try {
            val newsItem = NewsItem(title, summary, content, isPublished, "Hjelp?")
            supabaseClient.postgrest["news"].insert(newsItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Probably redundant as a similar data class exist elsewhere, here for testing
     *
     * TODO: Abstract :)
     */
    @Serializable
    data class NewsItem(
        val title: String,
        val summary: String?,
        val content: String?,
        val is_published: Boolean,
        val created_by: String
    )

}