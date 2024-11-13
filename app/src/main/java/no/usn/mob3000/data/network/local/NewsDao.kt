package no.usn.mob3000.data.network.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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

