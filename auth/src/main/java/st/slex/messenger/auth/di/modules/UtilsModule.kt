package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.data.utils.interf.TokenUtil
import st.slex.messenger.auth.data.utils.real.TokenUtilImpl
import st.slex.messenger.auth.ui.SendCodeEngine
import st.slex.messenger.auth.ui.utils.LoginHelper
import st.slex.messenger.auth.ui.utils.LoginHelperImpl

@Module
interface UtilsModule {

    @Binds
    fun bindsLoginHelper(engine: LoginHelperImpl): LoginHelper

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeEngine.Base): SendCodeEngine

    @Binds
    fun bindsTokenUtilHelper(util: TokenUtilImpl): TokenUtil
}