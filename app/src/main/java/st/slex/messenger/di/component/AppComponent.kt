package st.slex.messenger.di.component

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.MainActivity
import st.slex.messenger.di.module.RepositoryModule
import st.slex.messenger.di.module.ServiceModule
import st.slex.messenger.di.module.ViewModelFactoryModule
import st.slex.messenger.di.module.ViewModelModule
import st.slex.messenger.utilites.base.BaseFragment

@ExperimentalCoroutinesApi
@Component(
    modules = [
        RepositoryModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        ServiceModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}