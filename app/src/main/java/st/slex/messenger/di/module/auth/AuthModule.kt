package st.slex.messenger.di.module.auth

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.AuthActivity

@Module(
    includes = [
        EngineModule::class,
        AuthInterfaceModule::class
    ]
)
@ExperimentalCoroutinesApi
class AuthModule(val activity: AuthActivity) {

    @Provides
    fun provideActivity(): AuthActivity = activity
}