package st.slex.messenger.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.AppModule
import st.slex.messenger.di.module.ReferencesModule
import st.slex.messenger.di.module.ViewModelFactoryModule
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelFactoryModule::class,
        ReferencesModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun create(): AppComponent
    }

    val authBuilder: AuthSubcomponent.Builder
    val mainActivityBuilder: MainActivitySubcomponent.Builder
}