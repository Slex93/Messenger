package st.slex.messenger.di.component

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.ReferencesModule
import st.slex.messenger.di.module.RepositoryModule
import st.slex.messenger.di.module.ViewModelFactoryModule
import st.slex.messenger.di.module.ViewModelModule
import st.slex.messenger.ui.activities.MainActivity
import st.slex.messenger.utilites.base.RawFragment

@ExperimentalCoroutinesApi
@Component(
    modules = [
        RepositoryModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ReferencesModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: RawFragment)
}