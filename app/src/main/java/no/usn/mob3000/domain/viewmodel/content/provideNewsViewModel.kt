package no.usn.mob3000.domain.viewmodel.content

import android.content.Context
import no.usn.mob3000.data.repository.content.local.NewsRepositoryLocal
import no.usn.mob3000.data.repository.content.remote.NewsRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.content.NewsDataSource
import no.usn.mob3000.domain.usecase.content.news.DeleteNewsUseCase
import no.usn.mob3000.domain.usecase.content.news.FetchNewsUseCase
import no.usn.mob3000.domain.usecase.content.news.InsertNewsUseCase
import no.usn.mob3000.domain.usecase.content.news.UpdateNewsUseCase

/**
 * Provides newviewmodel with an instance. Instead of instancing everything trough the useCases, I use this support-class for each viewModel. Having interfaces
 * does cover some of this workarounds, but for extra measure i've collected all the initializations here.
 *
 * @author 258030
 * @created 2024-11-13
 */
fun provideNewsViewModel(context: Context): NewsViewModel {
    val newsRepositoryLocal = NewsRepositoryLocal(context)
    val newsRepository = NewsRepository(
        newsRepositoryLocal = newsRepositoryLocal,
        authDataSource = AuthDataSource(),
        newsDataSource = NewsDataSource()
    )
    return NewsViewModel(
        fetchNewsUseCase = FetchNewsUseCase(newsRepository),
        updateNewsUseCase = UpdateNewsUseCase(newsRepository),
        insertNewsUseCase = InsertNewsUseCase(newsRepository),
        deleteNewsUseCase = DeleteNewsUseCase(newsRepository)
    )
}
