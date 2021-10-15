package st.slex.messenger.di.module.main

import dagger.Module
import dagger.Provides
import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.contacts.ContactsData
import st.slex.messenger.data.contacts.ContactsDataMapper
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.contacts.ContactsUIMapper
import st.slex.messenger.ui.user_profile.UserUI

@Module
class MapperModule {

    @Provides
    fun providesChatsDataMapper(): Mapper.ToUI<List<ChatsData>, Resource<List<ChatsUI>>> =
        ChatsDataMapper()

    @Provides
    fun providesContactsDataMapper(): Mapper.ToUI<List<ContactsData>, Resource<List<ContactsUI>>> =
        ContactsDataMapper()

    @Provides
    fun providesUserDataMapper(): Mapper.ToUI<UserData, Resource<UserUI>> = UserDataMapper()

    @Provides
    fun providesContactsUIMapper(): Mapper.Data<List<ContactsUI>, List<ContactsData>> =
        ContactsUIMapper()
}