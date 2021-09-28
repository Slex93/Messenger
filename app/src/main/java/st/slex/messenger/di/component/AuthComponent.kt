package st.slex.messenger.di.component

import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.AuthActivity
import st.slex.messenger.ui.core.BaseAuthFragment

@ExperimentalCoroutinesApi
@Subcomponent()
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(activity: AuthActivity)
    fun inject(fragment: BaseAuthFragment)
}