package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.data.repository.AuthRepositoryImpl
import st.slex.messenger.auth.domain.interf.AuthRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsLoginRepository(repository: AuthRepositoryImpl): AuthRepository
}