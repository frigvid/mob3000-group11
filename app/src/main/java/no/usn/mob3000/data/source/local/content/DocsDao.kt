package no.usn.mob3000.data.source.local.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import no.usn.mob3000.data.model.content.local.DocsItemLocal

/**
 * Data Access Object (DAO) for the Docs entity.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Dao
interface DocsDao {
    @Query("SELECT * FROM docs_table")
    suspend fun getAllDocs(): List<DocsItemLocal>

    @Query("SELECT * FROM docs_table WHERE docsId = :docsId")
    suspend fun getDocsById(docsId: String): DocsItemLocal?

    @Query("DELETE FROM docs_table")
    suspend fun clearAllDocs()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaqList(docsList: List<DocsItemLocal>)

    @Update
    suspend fun updateFaq(docs: DocsItemLocal)

    @Delete
    suspend fun deleteFaq(docs: DocsItemLocal)
}
