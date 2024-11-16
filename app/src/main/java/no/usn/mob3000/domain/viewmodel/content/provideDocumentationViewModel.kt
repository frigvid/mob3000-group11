package no.usn.mob3000.domain.viewmodel.content

import android.content.Context
import no.usn.mob3000.data.repository.content.local.DocsRepositoryLocal
import no.usn.mob3000.data.repository.content.remote.DocsRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.docs.DocsDataSource
import no.usn.mob3000.domain.usecase.content.docs.DeleteDocsUseCase
import no.usn.mob3000.domain.usecase.content.docs.FetchDocUseCase
import no.usn.mob3000.domain.usecase.content.docs.InsertDocsUseCase
import no.usn.mob3000.domain.usecase.content.docs.UpdateDocsUseCase
/**
 * Provides documentationViewModel with an instance. Instead of instancing everything trough the useCases, I use this support-class for each viewModel.
 * Having interfaces does cover some of this workarounds, but for extra measure i've collected all the initializations here.
 *
 * @author 258030
 * @created 2024-11-13
 */
fun provideDocumentationViewModel(context: Context): DocumentationViewModel {
    val docsRepositoryLocal = DocsRepositoryLocal(context)
    val docsRepository = DocsRepository(
        docsRepositoryLocal = docsRepositoryLocal,
        authDataSource = AuthDataSource(),
        docsDataSource = DocsDataSource()
    )
    return DocumentationViewModel(
        fetchDocUseCase = FetchDocUseCase(docsRepository),
        deleteDocsUseCase = DeleteDocsUseCase(docsRepository),
        updateDocsUseCase = UpdateDocsUseCase(docsRepository),
        insertDocsUseCase = InsertDocsUseCase(docsRepository)
    )
}
