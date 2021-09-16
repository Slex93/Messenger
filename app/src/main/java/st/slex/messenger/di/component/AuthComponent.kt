package st.slex.messenger.di.component

import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.activities.AuthActivity

@ExperimentalCoroutinesApi
@Subcomponent()
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(activity: AuthActivity)
}