package st.slex.messenger.di.component

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.module.ReferencesModule
import st.slex.messenger.di.module.auth.AuthModule
import st.slex.messenger.ui.core.BaseAuthFragment

@ExperimentalCoroutinesApi
@Component(
    modules = [
        AuthModule::class,
        ReferencesModule::class
    ]
)
interface AuthComponent {
    fun inject(fragment: BaseAuthFragment)
}