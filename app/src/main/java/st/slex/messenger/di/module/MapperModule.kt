package st.slex.messenger.di.module

import dagger.Module
import dagger.Provides
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.domain.chats.ChatsDomain
import st.slex.messenger.ui.main_screen.ChatsUI

@Module
class MapperModule {

    @Provides
    fun bindsChatsDataMapper(): ChatsData.ChatsDataMapper<ChatsDomain> =
        ChatsData.ChatsDataMapper.Base()

    @Provides
    fun bindsChatsDomainMapper(): ChatsDomain.ChatsDomainMapper<ChatsUI> =
        ChatsDomain.ChatsDomainMapper.Base()

}