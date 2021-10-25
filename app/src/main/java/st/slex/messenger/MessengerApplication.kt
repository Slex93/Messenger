package st.slex.messenger

import android.app.Application
import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.application.AppComponent
import st.slex.messenger.di.application.DaggerAppComponent

@ExperimentalCoroutinesApi
class MessengerApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().application(this).create()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}

@ExperimentalCoroutinesApi
val Context.appComponent: AppComponent
    get() = when (this) {
        is MessengerApplication -> appComponent
        else -> (applicationContext as MessengerApplication).appComponent
    }