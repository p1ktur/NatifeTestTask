package com.task.feature_gifs.domain.koin

import com.task.feature_gifs.data.dataSources.LocalGifsDataSource
import com.task.feature_gifs.data.dataSources.RemoteGifsDataSource
import com.task.feature_gifs.data.repositories.SearchRepository
import com.task.feature_gifs.domain.model.connection.ConnectionManager
import com.task.feature_gifs.domain.stateStores.shared.SearchGifViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val koinGifsModule = module {
    single { ConnectionManager(androidContext()) }

    single { LocalGifsDataSource.getInstance(androidContext()) }
    single { get<LocalGifsDataSource>().getGifDao() }

    single { RemoteGifsDataSource() }
    single { SearchRepository(get(), get(), get()) }

    viewModel { SearchGifViewModel(get(), get()) }
}