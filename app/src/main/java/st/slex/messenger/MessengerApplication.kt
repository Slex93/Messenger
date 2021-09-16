package st.slex.messenger

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.component.AppComponent
import st.slex.messenger.di.component.DaggerAppComponent

@ExperimentalCoroutinesApi
class MessengerApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}