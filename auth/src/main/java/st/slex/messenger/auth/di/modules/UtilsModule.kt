package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.data.utils.interf.TokenUtil
import st.slex.messenger.auth.data.utils.real.TokenUtilImpl
import st.slex.messenger.auth.ui.use_case.impl.LoginUseCaseImpl
import st.slex.messenger.auth.ui.use_case.impl.SendCodeUseCaseImpl
import st.slex.messenger.auth.ui.use_case.interf.LoginUseCase
import st.slex.messenger.auth.ui.use_case.interf.SendCodeUseCase

@Module
interface UtilsModule {

    @Binds
    fun bindsLoginHelper(engine: LoginUseCaseImpl): LoginUseCase

    @Binds
    fun bindsSendCodeEngine(engine: SendCodeUseCaseImpl): SendCodeUseCase

    @Binds
    fun bindsTokenUtilHelper(util: TokenUtilImpl): TokenUtil
}