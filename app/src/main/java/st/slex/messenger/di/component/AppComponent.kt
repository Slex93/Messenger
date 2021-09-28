package st.slex.messenger.di.component

import android.app.Application
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.*
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.main.MainActivity
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivitiesModule::class,
        RepositoryModule::class,
        InteractorModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ReferencesModule::class,
        MapperModule::class,
        ResponseModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun applicationBind(application: Application): Builder
    }

    fun inject(application: Application)
    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
    fun inject(fragment: BaseAuthFragment)
}