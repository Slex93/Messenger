package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import st.slex.messenger.domain.impl.LoginEngineImpl
import st.slex.messenger.domain.impl.SendCodeEngineImpl
import st.slex.messenger.domain.interf.LoginEngine
import st.slex.messenger.domain.interf.SendCodeEngine

@Module
interface EngineModule {
    @Binds
    fun bindsLoginEngine(engine: LoginEngineImpl): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngineImpl): SendCodeEngine
}