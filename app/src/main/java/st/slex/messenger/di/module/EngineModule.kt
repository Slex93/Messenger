package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.auth.engine.impl.LoginEngineImpl
import st.slex.messenger.ui.auth.engine.impl.SendCodeEngineImpl
import st.slex.messenger.ui.auth.engine.interf.LoginEngine
import st.slex.messenger.ui.auth.engine.interf.SendCodeEngine

@ExperimentalCoroutinesApi
@Module
interface EngineModule {
    @Binds
    fun bindsLoginEngine(engine: LoginEngineImpl): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngineImpl): SendCodeEngine
}