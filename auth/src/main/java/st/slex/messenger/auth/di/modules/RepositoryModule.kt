package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import st.slex.messenger.auth.data.AuthRepository

@Module
interface RepositoryModule {

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Binds
    fun bindsLoginRepository(repository: AuthRepository.Base): AuthRepository
}