package st.slex.messenger.di.component

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.*
import st.slex.messenger.ui.activities.AuthActivity
import st.slex.messenger.ui.activities.MainActivity
import st.slex.messenger.utilites.base.RawFragment

@ExperimentalCoroutinesApi
@Component(
    modules = [
        RepositoryModule::class,
        InteractorModule::class,
        EngineModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ReferencesModule::class,
        MapperModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: AuthActivity)
    fun inject(fragment: RawFragment)
}