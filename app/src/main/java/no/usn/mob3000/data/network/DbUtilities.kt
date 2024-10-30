package no.usn.mob3000.data.network

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * A utility class for performing database operations.
 *
 *
 * @author Eirik
 * @created 2024-10-28
 */
class DbUtilities {

    /**
     * Inserts an item into a database table.
     *
     * @param table The name of the database table.
     * @param item The item to be inserted.
     * @param serializer The serializer for the item type.
     * @author Eirik
     * @created 2024-10-28
     */
    suspend fun <T> insertItem(table: String, item: T, serializer: KSerializer<T>): Result<Unit> {
        return try {
            val jsonElement: JsonElement = Json.encodeToJsonElement(serializer, item)
            SupabaseClientWrapper.getClient().postgrest[table].insert(jsonElement)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches items from a database table.
     * Must admit that i let Android Studio (or IntelliJ autocorrect) get the better of me. I think this could look a lot cleaner in a perfect world.
     *
     * Todo: Reverse engineer this. Try to shorten this as much as possible.
     *
     * @param tableName The name of the database table.
     * @param serializer The serializer for the item type.
     * @return A [Result] containing a list of items or an exception.
     * @author Eirik
     * @created 2024-10-28
     */
    suspend inline fun <reified T : Any> fetchItems(tableName: String, serializer: KSerializer<T>): Result<List<T>> {
        return try {
            val supabaseClient = SupabaseClientWrapper.getClient()
            val result = withContext(Dispatchers.IO) {
                supabaseClient.from(tableName).select().decodeList<T>()
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes an item in a database table.
     *
     * @param table The name of the database table.
     * @param fieldName The name of the field to filter by.
     * @param fieldValue The value to filter by.
     * @return A [Result] indicating the success or failure of the operation.
     * @author Eirik
     * @created 2024-10-28
     */
    suspend fun deleteItem(table: String, fieldName: String, fieldValue: String): Result<Unit> {
        return try {
            SupabaseClientWrapper.getClient().postgrest.from(table).delete {
                filter { eq(fieldName, fieldValue) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




}
