package st.slex.messenger.di.component

import dagger.Component
import st.slex.messenger.MainActivity
import st.slex.messenger.di.module.RepositoryModule
import st.slex.messenger.di.module.ViewModelFactoryModule
import st.slex.messenger.di.module.ViewModelModule
import st.slex.messenger.utilites.base.BaseFragment

@Component(
    modules = [
        RepositoryModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}