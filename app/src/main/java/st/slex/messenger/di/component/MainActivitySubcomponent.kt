package st.slex.messenger.di.component

import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.main.MainActivityModule
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.main.MainActivity

@ExperimentalCoroutinesApi
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubcomponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: MainActivity): Builder

        fun create(): MainActivitySubcomponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}