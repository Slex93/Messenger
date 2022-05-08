package st.slex.messenger.auth.di

import dagger.BindsInstance
import dagger.Component
import st.slex.messenger.auth.di.scopes.AuthScope
import st.slex.messenger.auth.ui.AuthActivity
import st.slex.messenger.auth.ui.EnterCodeFragment
import st.slex.messenger.auth.ui.EnterPhoneFragment
import javax.inject.Singleton

@AuthScope
@Singleton
@Component(modules = [AuthModule::class])
interface AuthComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: AuthActivity): Builder
        fun create(): AuthComponent
    }

    fun inject(fragment: EnterPhoneFragment)
    fun inject(fragment: EnterCodeFragment)
}