package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.data.utils.interf.TokenUtil
import st.slex.messenger.auth.data.utils.real.TokenUtilImpl
import st.slex.messenger.auth.ui.LoginEngine
import st.slex.messenger.auth.ui.SendCodeEngine

@Module
interface UtilsModule {

    @Binds
    fun bindsLoginEngine(engine: LoginEngine.Base): LoginEngine

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngine.Base): SendCodeEngine

    @Binds
    fun bindsTokenUtilHelper(util: TokenUtilImpl): TokenUtil
}