package st.slex.messenger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun create(): AppComponent
    }

    @ExperimentalCoroutinesApi
    fun inject(activity: SplashActivity)
}