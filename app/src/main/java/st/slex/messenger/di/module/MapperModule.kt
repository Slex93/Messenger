package st.slex.messenger.di.module

import dagger.Module
import dagger.Provides
import st.slex.messenger.core.Abstract
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.contacts.ContactsData
import st.slex.messenger.data.contacts.ContactsDataMapper
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.user_profile.UserUI

@Module
class MapperModule {

    @Provides
    fun providesChatsDataMapper(): Abstract.Mapper.DataToUi<List<ChatsData>, UIResult<List<ChatsUI>>> =
        ChatsDataMapper()

    @Provides
    fun providesContactsDataMapper(): Abstract.Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactsUI>>> =
        ContactsDataMapper()

    @Provides
    fun providesUserDataMapper(): Abstract.Mapper.DataToUi<UserData, UIResult<UserUI>> =
        UserDataMapper()

}