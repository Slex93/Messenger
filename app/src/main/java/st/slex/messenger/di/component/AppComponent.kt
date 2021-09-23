package st.slex.messenger.di.component

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
        RepositoryModule::class,
        InteractorModule::class,
        EngineModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ReferencesModule::class,
        MapperModule::class,
        SubcomponentsModule::class,
        ResponseModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
    fun authComponent(): AuthComponent.Factory
}