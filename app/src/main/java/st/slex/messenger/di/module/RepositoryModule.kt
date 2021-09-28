package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.data.chat.SingleChatRepository
import st.slex.messenger.data.chats.ChatsRepository
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.data.main_activity.ActivityRepository
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.data.settings.SettingsRepository

@ExperimentalCoroutinesApi
@Module
interface RepositoryModule {
    @Binds
    fun bindActivityRepository(repository: ActivityRepository.Base): ActivityRepository

    @Binds
    fun bindContactRepository(repository: ContactsRepository.Base): ContactsRepository

    @Binds
    fun bindSingleChatRepository(repository: SingleChatRepository.Base): SingleChatRepository

    @Binds
    fun bindSettingsRepository(repository: SettingsRepository.Base): SettingsRepository

    @Binds
    fun bindUserRepository(repository: UserRepository.Base): UserRepository

    @Binds
    fun bindChatsRepository(repository: ChatsRepository.Base): ChatsRepository
}