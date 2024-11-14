package no.usn.mob3000.domain.viewmodel.content

import android.content.Context
import no.usn.mob3000.data.repository.content.local.FAQRepositoryLocal
import no.usn.mob3000.data.repository.content.remote.FAQRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.docs.FAQDataSource
import no.usn.mob3000.domain.usecase.content.faq.DeleteFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.FetchFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.InsertFAQUseCase
import no.usn.mob3000.domain.usecase.content.faq.UpdateFAQUseCase

/**
 * Provide FAQViewModel with an instance.
 *
 * @author 258030
 * @created 2024-11-13
 */
fun provideFAQViewModel(context: Context): FAQViewModel {
    val faqRepositoryLocal = FAQRepositoryLocal(context)
    val faqRepository = FAQRepository(
        faqRepositoryLocal = faqRepositoryLocal,
        authDataSource = AuthDataSource(),
        faqDataSource = FAQDataSource()
    )
    return FAQViewModel(
        fetchFAQUseCase = FetchFAQUseCase(faqRepository),
        updateFAQUseCase = UpdateFAQUseCase(faqRepository),
        insertFAQUseCase = InsertFAQUseCase(faqRepository),
        deleteFAQUseCase = DeleteFAQUseCase(faqRepository)
    )
}


