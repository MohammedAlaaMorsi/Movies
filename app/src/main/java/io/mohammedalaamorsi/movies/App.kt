package io.mohammedalaamorsi.movies

import android.app.Application
import io.mohammedalaamorsi.movies.di.appModule
import io.mohammedalaamorsi.movies.di.dataModule
import io.mohammedalaamorsi.movies.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                appModule,
                dataModule,
                domainModule,
            )
        }
    }
}
