package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.auth.data.repository.AuthRepositoryImpl
import st.slex.messenger.auth.domain.interf.AuthRepository

@Module
interface RepositoryModule {

    @Binds
    fun bindsLoginRepository(repository: AuthRepositoryImpl): AuthRepository
}