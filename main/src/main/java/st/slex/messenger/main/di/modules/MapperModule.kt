package st.slex.messenger.main.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.main.data.chats.ChatsDataMapper
import st.slex.messenger.main.data.contacts.ContactDataMapper
import st.slex.messenger.main.data.contacts.ContactListDataMapper
import st.slex.messenger.main.data.user.UserDataMapper
import st.slex.messenger.main.ui.contacts.ContactsUIMapper

@Module
interface MapperModule {

    @Binds
    fun bindsChatsDataMapper(mapper: ChatsDataMapper.Base): ChatsDataMapper

    @Binds
    fun bindsContactListDataMapper(mapper: ContactListDataMapper.Base): ContactListDataMapper

    @Binds
    fun bindsUserDataMapper(mapper: UserDataMapper.Base): UserDataMapper

    @Binds
    fun bindsContactsUIMapper(mapper: ContactsUIMapper.Base): ContactsUIMapper

    @Binds
    fun bindsContactDataMapper(mapper: ContactDataMapper.Base): ContactDataMapper
}