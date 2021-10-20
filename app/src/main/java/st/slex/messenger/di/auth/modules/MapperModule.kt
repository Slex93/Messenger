package st.slex.messenger.di.auth.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.domain.LoginDomainMapper

@Module
interface MapperModule {

    @Binds
    fun bindsLoginMapper(mapper: LoginDomainMapper.Base): LoginDomainMapper
}