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
 * Provides newviewmodel with an instance. Had to to it like this since it kept getting under my skin that i could not initiate it directly in the viewmodel. I think it's normal to use
 * some framework for this type of initiating, since the repositories has to many parameters to be initiated directly. Will be refactoring later on, so we might be able to fix this with just
 * fewer parameters.
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



