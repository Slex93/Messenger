package st.slex.messenger.auth.di

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.ui.AuthActivity
import st.slex.messenger.auth.ui.BaseAuthFragment

@AuthScope
@ExperimentalCoroutinesApi
@Component(modules = [AuthModule::class])
interface AuthComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: AuthActivity): Builder
        fun create(): AuthComponent
    }

    fun inject(fragment: BaseAuthFragment)
}