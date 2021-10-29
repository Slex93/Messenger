package st.slex.messenger

import android.app.Application
import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MessengerApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().application(this).create()
    }
}

/*Would be useful*/
@ExperimentalCoroutinesApi
val Context.appComponent: AppComponent
    get() = when (this) {
        is MessengerApplication -> appComponent
        else -> (applicationContext as MessengerApplication).appComponent
    }