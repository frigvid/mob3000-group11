package no.usn.mob3000.data.repository.content.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.local.FaqItemLocal
import no.usn.mob3000.data.network.local.AppDatabase
import no.usn.mob3000.data.source.local.content.FaqDao


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
