package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.data.chats.ChatsRepository
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.data.repository.impl.*
import st.slex.messenger.data.repository.interf.*

@ExperimentalCoroutinesApi
@Module
interface RepositoryModule {
    @Binds
    fun bindActivityRepository(repository: ActivityRepositoryImpl): ActivityRepository

    @Binds
    fun bindsLoginRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindContactRepository(repository: ContactsRepository.Base): ContactsRepository

    @Binds
    fun bindSingleChatRepository(repository: SingleChatRepositoryImpl): SingleChatRepository

    @Binds
    fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    @Binds
    fun bindUserRepositoryImpl(repository: UserRepositoryImpl): UserRepository

    @Binds
    fun bindChatsTestRepository(repository: ChatsRepository.Base): ChatsRepository
}