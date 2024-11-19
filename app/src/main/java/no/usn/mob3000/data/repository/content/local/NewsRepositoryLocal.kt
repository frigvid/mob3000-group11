package no.usn.mob3000.data.repository.content.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.local.NewsItemLocal
import no.usn.mob3000.data.source.local.content.NewsDao
import no.usn.mob3000.data.source.local.roomDb.AppDatabase

/**
 * Repository class responsible for managing operations related to the news table. It uses [NewsDao] for fetching and handling
 * It also keeps the initiation of local and remote data sources from each-other.
 *
 * @param context The application context.
 * @author 258030
 * @created 2024-11-13
 */
class NewsRepositoryLocal(
    private val context: Context
) {
    private val newsDao: NewsDao by lazy {
        AppDatabase.getInstance(context).newsDao()
    }

    suspend fun insertNewsList(newsList: List<NewsItemLocal>) = withContext(Dispatchers.IO) {
        newsDao.insertNewsList(newsList)
    }

    suspend fun fetchAllNews(): List<NewsItemLocal> = withContext(Dispatchers.IO) {
        newsDao.getAllNews()
    }

    suspend fun clearAllNews() = withContext(Dispatchers.IO) {
        newsDao.clearAllNews()
    }
}
