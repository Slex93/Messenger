package st.slex.messenger.di.component

import dagger.BindsInstance
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.auth.AuthModule
import st.slex.messenger.ui.auth.AuthActivity
import st.slex.messenger.ui.core.BaseAuthFragment

@ExperimentalCoroutinesApi
@Subcomponent(
    modules = [AuthModule::class]
)
interface AuthSubcomponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: AuthActivity): Builder

        fun create(): AuthSubcomponent
    }

    fun inject(fragment: BaseAuthFragment)
}