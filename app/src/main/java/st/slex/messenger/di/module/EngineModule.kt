package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import st.slex.messenger.domain.engine.impl.LoginEngineImpl
import st.slex.messenger.domain.engine.impl.SendCodeEngineImpl
import st.slex.messenger.domain.engine.interf.LoginEngine
import st.slex.messenger.domain.engine.interf.SendCodeEngine

@Module
interface EngineModule {
    @Binds
    fun bindsLoginEngine(engine: LoginEngineImpl): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngineImpl): SendCodeEngine
}