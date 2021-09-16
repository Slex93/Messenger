package st.slex.messenger.di.module

import dagger.Module
import dagger.Provides
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.contacts.ContactsDataMapper
import st.slex.messenger.domain.chats.ChatsDomainMapper
import st.slex.messenger.domain.chats.ChatsDomainResult
import st.slex.messenger.domain.contacts.ContactsDomainMapper
import st.slex.messenger.domain.contacts.ContactsDomainResult
import st.slex.messenger.ui.chats.ChatsUIResult
import st.slex.messenger.ui.contacts.ContactsUIResult

@Module
class MapperModule {

    @Provides
    fun providesChatsDataMapper(): ChatsDataMapper<ChatsDomainResult> =
        ChatsDataMapper.Base()

    @Provides
    fun providesChatsDomainMapper(): ChatsDomainMapper<ChatsUIResult> =
        ChatsDomainMapper.Base()

    @Provides
    fun providesContactsDataMapper(): ContactsDataMapper<ContactsDomainResult> =
        ContactsDataMapper.Base()

    @Provides
    fun providesContactsDomainMapper(): ContactsDomainMapper<ContactsUIResult> =
        ContactsDomainMapper.Base()

}