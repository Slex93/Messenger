package st.slex.messenger.main.di.modules

import dagger.Module
import dagger.Provides
import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.chats.ChatsData
import st.slex.messenger.main.data.chats.ChatsDataMapper
import st.slex.messenger.main.data.contacts.ContactData
import st.slex.messenger.main.data.contacts.ContactDataMapper
import st.slex.messenger.main.data.contacts.ContactListDataMapper
import st.slex.messenger.main.data.user.UserData
import st.slex.messenger.main.data.user.UserDataMapper
import st.slex.messenger.main.ui.chats.ChatsUI
import st.slex.messenger.main.ui.contacts.ContactUI
import st.slex.messenger.main.ui.contacts.ContactsUIMapper
import st.slex.messenger.main.ui.user_profile.UserUI

@Module
class MapperModule {

    @Provides
    fun providesChatsDataMapper(): Mapper.ToUI<List<ChatsData>, Resource<List<ChatsUI>>> =
        ChatsDataMapper()

    @Provides
    fun providesContactsDataMapper(): Mapper.ToUI<List<ContactData>, Resource<List<ContactUI>>> =
        ContactListDataMapper()

    @Provides
    fun providesUserDataMapper(): Mapper.ToUI<UserData, Resource<UserUI>> = UserDataMapper()

    @Provides
    fun providesContactsUIMapper(): Mapper.Data<List<ContactUI>, List<ContactData>> =
        ContactsUIMapper()

    @Provides
    fun providesContactDataMapper(): Mapper.ToUI<ContactData, Resource<ContactUI>> =
        ContactDataMapper()
}