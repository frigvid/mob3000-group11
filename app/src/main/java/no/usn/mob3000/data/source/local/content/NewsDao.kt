package no.usn.mob3000.data.source.local.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import no.usn.mob3000.data.model.content.local.NewsItemLocal

/**
 * Data Access Object (DAO) interface for the news table.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    suspend fun getAllNews(): List<NewsItemLocal>

    @Query("SELECT * FROM news_table WHERE newsId = :newsId")
    suspend fun getNewsById(newsId: String): NewsItemLocal?

    @Query("DELETE FROM news_table")
    suspend fun clearAllNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(newsList: List<NewsItemLocal>)

    @Update
    suspend fun updateNews(news: NewsItemLocal)

    @Delete
    suspend fun deleteNews(news: NewsItemLocal)
}

