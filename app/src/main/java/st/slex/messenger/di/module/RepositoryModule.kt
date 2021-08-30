package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.data.repository.impl.*
import st.slex.messenger.data.repository.interf.*

@ExperimentalCoroutinesApi
@Module
interface RepositoryModule {
    @Binds
    fun bindActivityRepository(repository: ActivityRepositoryImpl): ActivityRepository

    @Binds
    fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindMainRepository(repository: MainRepositoryImpl): MainRepository

    @Binds
    fun bindContactRepository(repository: ContactsRepositoryImpl): ContactsRepository

    @Binds
    fun bindSingleChatRepository(repository: SingleChatRepositoryImpl): SingleChatRepository

    @Binds
    fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
}