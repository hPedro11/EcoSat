package br.com.gs.ecosat

import android.app.Application
import br.com.gs.ecosat.di.dataModule
import br.com.gs.ecosat.di.domainModule
import br.com.gs.ecosat.di.networkModule
import br.com.gs.ecosat.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EcoSatApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EcoSatApplication)

            modules(
                networkModule,
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}