package st.slex.messenger.main.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.data.chats.ChatsRepository
import st.slex.messenger.main.data.contacts.ContactsRepository
import st.slex.messenger.main.data.main_activity.ActivityRepository
import st.slex.messenger.main.data.settings.SettingsRepository
import st.slex.messenger.main.data.single_chat.SingleChatRepository
import st.slex.messenger.main.data.user.UserRepository

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