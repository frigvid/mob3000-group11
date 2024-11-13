package no.usn.mob3000.data.network.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepositoryLocal(private val context: Context) {

    private val newsDao: NewsDao by lazy {
        AppDatabase.getInstance(context).newsDao()
    }

    suspend fun insertNewsList(newsList: List<NewsItemLocal>) = withContext(Dispatchers.IO) {
        newsDao.insertNewsList(newsList)
    }

    suspend fun fetchAllNews(): List<NewsItemLocal> = withContext(Dispatchers.IO) {
        newsDao.getAllNews()
    }

    suspend fun fetchNewsById(newsId: String): NewsItemLocal? = withContext(Dispatchers.IO) {
        newsDao.getNewsById(newsId)
    }

    suspend fun clearAllNews() = withContext(Dispatchers.IO) {
        newsDao.clearAllNews()
    }


    suspend fun updateNews(news: NewsItemLocal) = withContext(Dispatchers.IO) {
        newsDao.updateNews(news)
    }

    suspend fun deleteNews(news: NewsItemLocal) = withContext(Dispatchers.IO) {
        newsDao.deleteNews(news)
    }
}


