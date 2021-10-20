package st.slex.messenger.di.main_activity

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.application.AppComponent
import st.slex.messenger.di.scope.MainActivityScope
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.main.MainActivity

@MainActivityScope
@ExperimentalCoroutinesApi
@Component(
    dependencies = [AppComponent::class],
    modules = [MainActivityModule::class]
)
interface MainActivityComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: MainActivity): Builder
        fun appComponent(appComponent: AppComponent): Builder
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: BaseFragment)
}