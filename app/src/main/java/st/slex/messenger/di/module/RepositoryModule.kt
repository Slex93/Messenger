package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import st.slex.messenger.data.repository.impl.ActivityRepositoryImpl
import st.slex.messenger.data.repository.impl.AuthRepositoryImpl
import st.slex.messenger.data.repository.impl.MainRepositoryImpl
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.data.repository.interf.MainRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindActivityRepository(repository: ActivityRepositoryImpl): ActivityRepository

    @Binds
    fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindMainRepository(repository: MainRepositoryImpl): MainRepository
}