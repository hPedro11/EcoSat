package br.com.gs.ecosat.di

import br.com.gs.ecosat.data.local.PreferencesDataSource
import br.com.gs.ecosat.data.local.PreferencesDataSourceImpl
import br.com.gs.ecosat.data.remote.EventRemoteDataSource
import br.com.gs.ecosat.data.remote.EventRemoteDataSourceImpl
import br.com.gs.ecosat.data.repository.EventRepositoryImpl
import br.com.gs.ecosat.data.repository.PreferencesRepositoryImpl
import br.com.gs.ecosat.domain.repository.EventRepository
import br.com.gs.ecosat.domain.repository.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // Remote
    single<EventRemoteDataSource> {
        EventRemoteDataSourceImpl(api = get())
    }

    // Local
    single<PreferencesDataSource> {
        PreferencesDataSourceImpl(context = androidContext())
    }

    // Repositories
    single<EventRepository> {
        EventRepositoryImpl(
            remoteDataSource = get(),
            classifyRisk = get()
        )
    }

    single<PreferencesRepository> {
        PreferencesRepositoryImpl(dataSource = get())
    }
}