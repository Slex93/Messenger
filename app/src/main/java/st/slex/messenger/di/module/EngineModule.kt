package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.LoginEngine
import st.slex.messenger.ui.auth.SendCodeEngine

@ExperimentalCoroutinesApi
@Module
interface EngineModule {
    @Binds
    fun bindsLoginEngine(engine: LoginEngine.Base): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngine.Base): SendCodeEngine
}