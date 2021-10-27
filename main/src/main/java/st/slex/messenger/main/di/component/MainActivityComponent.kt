package st.slex.messenger.main.di.component

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.di.scope.MainActivityScope
import st.slex.messenger.main.ui.MainActivity
import st.slex.messenger.main.ui.core.BaseFragment

@MainActivityScope
@ExperimentalCoroutinesApi
@Component(modules = [MainActivityModule::class])
interface MainActivityComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: MainActivity): Builder
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}