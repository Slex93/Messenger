package st.slex.messenger.di.auth.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import st.slex.messenger.data.auth.AuthRepository

@Module
interface RepositoryModule {

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Binds
    fun bindsLoginRepository(repository: AuthRepository.Base): AuthRepository
}