package st.slex.messenger.di.main_activity.modules

import dagger.Module
import dagger.Provides
import st.slex.messenger.auth.core.Mapper
import st.slex.messenger.auth.core.Resource
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.contacts.ContactData
import st.slex.messenger.data.contacts.ContactDataMapper
import st.slex.messenger.data.contacts.ContactsDataMapper
import st.slex.messenger.data.user.UserData
import st.slex.messenger.data.user.UserDataMapper
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.contacts.ContactUI
import st.slex.messenger.ui.contacts.ContactsUIMapper
import st.slex.messenger.ui.user_profile.UserUI

@Module
class MapperModule {

    @Provides
    fun providesChatsDataMapper(): Mapper.ToUI<List<ChatsData>, Resource<List<ChatsUI>>> =
        ChatsDataMapper()

    @Provides
    fun providesContactsDataMapper(): Mapper.ToUI<List<ContactData>, Resource<List<ContactUI>>> =
        ContactsDataMapper()

    @Provides
    fun providesUserDataMapper(): Mapper.ToUI<UserData, Resource<UserUI>> = UserDataMapper()

    @Provides
    fun providesContactsUIMapper(): Mapper.Data<List<ContactUI>, List<ContactData>> =
        ContactsUIMapper()

    @Provides
    fun providesContactDataMapper(): Mapper.ToUI<ContactData, Resource<ContactUI>> =
        ContactDataMapper()
}