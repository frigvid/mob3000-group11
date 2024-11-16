package no.usn.mob3000.data.source.local.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import no.usn.mob3000.data.model.content.local.FaqItemLocal
/**
 * Data Access Object (DAO) for the Faq entity.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Dao
interface FaqDao {

    @Query("SELECT * FROM faq_table")
    suspend fun getAllFaq(): List<FaqItemLocal>

    @Query("SELECT * FROM faq_table WHERE faqId = :faqId")
    suspend fun getFaqById(faqId: String): FaqItemLocal?

    @Query("DELETE FROM faq_table")
    suspend fun clearAllFaq()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaqList(faqList: List<FaqItemLocal>)

    @Update
    suspend fun updateFaq(faq: FaqItemLocal)

    @Delete
    suspend fun deleteFaq(faq: FaqItemLocal)
}
