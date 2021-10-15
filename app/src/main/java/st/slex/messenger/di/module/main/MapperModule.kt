package st.slex.messenger.di.module.main

import dagger.Module
import dagger.Provides
import st.slex.messenger.core.Mapper
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.contacts.ContactsData
import st.slex.messenger.data.contacts.ContactsDataMapper
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.contacts.ContactUIModel
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.user_profile.UserUI

@Module
class MapperModule {

    @Provides
    fun providesChatsDataMapper(): Mapper.DataToUi<List<ChatsData>, UIResult<List<ChatsUI>>> =
        ChatsDataMapper()

    @Provides
    fun providesContactsDataMapper(): Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactUIModel>>> =
        ContactsDataMapper()

    @Provides
    fun providesUserDataMapper(): Mapper.DataToUi<UserData, UIResult<UserUI>> = UserDataMapper()
}