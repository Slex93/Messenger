package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.domain.interf.LoginDomainMapper
import st.slex.messenger.auth.domain.real.LoginDomainMapperImpl

@Module
interface MapperModule {

    @Binds
    fun bindsLoginMapper(mapper: LoginDomainMapperImpl): LoginDomainMapper
}