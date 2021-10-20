package st.slex.messenger

import android.app.Application
import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.application.AppComponent
import st.slex.messenger.di.application.DaggerAppComponent

@ExperimentalCoroutinesApi
class MessengerApplication : Application() {
    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = checkNotNull(_appComponent)

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder()
            .application(this)
            .create()
    }
}

@ExperimentalCoroutinesApi
val Context.appComponent: AppComponent
    get() = when (this) {
        is MessengerApplication -> appComponent
        else -> (applicationContext as MessengerApplication).appComponent
    }