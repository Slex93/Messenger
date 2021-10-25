package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.ui.LoginEngine
import st.slex.messenger.auth.ui.SendCodeEngine

@ExperimentalCoroutinesApi
@Module
interface EngineModule {

    @Binds
    fun bindsLoginEngine(engine: LoginEngine.Base): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngine.Base): SendCodeEngine
}