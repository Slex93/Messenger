package st.slex.messenger

import android.app.Application
import st.slex.messenger.di.component.AppComponent
import st.slex.messenger.di.component.DaggerAppComponent

class MessengerApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}