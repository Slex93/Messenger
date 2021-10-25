package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.domain.LoginDomainMapper

@Module
interface MapperModule {

    @Binds
    fun bindsLoginMapper(mapper: LoginDomainMapper.Base): LoginDomainMapper
}