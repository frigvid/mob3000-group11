package no.usn.mob3000.data.repository.content.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.local.FaqItemLocal
import no.usn.mob3000.data.source.local.roomDb.AppDatabase
import no.usn.mob3000.data.source.local.content.FaqDao

/**
 * Repository class responsible for managing operations related to the faq table. It uses [FaqDao] for fetching and handling
 *
 * @param context The application context.
 * @author 258030
 * @created 2024-11-13
 */
class FAQRepositoryLocal(private val context: Context) {

    private val faqDao: FaqDao by lazy {
        AppDatabase.getInstance(context).faqDao()
    }
    suspend fun insertFaqList(faqList: List<FaqItemLocal>) = withContext(Dispatchers.IO) {
        faqDao.insertFaqList(faqList)
    }
    suspend fun fetchAllFaq(): List<FaqItemLocal> = withContext(Dispatchers.IO) {
        faqDao.getAllFaq()
    }
    suspend fun clearAllFaq() = withContext(Dispatchers.IO) {
        faqDao.clearAllFaq()
    }
}
