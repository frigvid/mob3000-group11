package no.usn.mob3000.data.repository.content.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.local.DocsItemLocal
import no.usn.mob3000.data.source.local.content.DocsDao
import no.usn.mob3000.data.source.local.roomDb.AppDatabase

/**
 * Repository class responsible for managing operations related to the docs table. It uses [DocsDao] for fetching and handling
 *
 * @param context The application context.
 * @author 258030
 * @created 2024-11-13
 */
class DocsRepositoryLocal(
    private val context: Context
) {
    private val docsDao: DocsDao by lazy {
        AppDatabase.getInstance(context).docsDao()
    }

    suspend fun insertDocsList(docsList: List<DocsItemLocal>) = withContext(Dispatchers.IO) {
        docsDao.insertFaqList(docsList)
    }

    suspend fun fetchAllDocs(): List<DocsItemLocal> = withContext(Dispatchers.IO) {
        docsDao.getAllDocs()
    }

    suspend fun clearAllDocs() = withContext(Dispatchers.IO) {
        docsDao.clearAllDocs()
    }
}
