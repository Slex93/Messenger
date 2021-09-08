package st.slex.messenger.di.component

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.activities.MainActivity
import st.slex.messenger.di.module.*
import st.slex.messenger.utilites.base.BaseFragment

@ExperimentalCoroutinesApi
@Component(
    modules = [
        RepositoryModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ServiceModule::class,
        ReferencesModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}