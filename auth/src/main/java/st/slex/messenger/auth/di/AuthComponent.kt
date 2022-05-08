package st.slex.messenger.auth.di

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.di.scopes.AuthScope
import st.slex.messenger.auth.ui.AuthActivity
import st.slex.messenger.auth.ui.EnterCodeFragment
import st.slex.messenger.auth.ui.EnterPhoneFragment

@AuthScope
@Component(modules = [AuthModule::class])
interface AuthComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: AuthActivity): Builder
        fun create(): AuthComponent
    }

    @ExperimentalCoroutinesApi
    fun inject(fragment: EnterPhoneFragment)

    @ExperimentalCoroutinesApi
    fun inject(fragment: EnterCodeFragment)
}