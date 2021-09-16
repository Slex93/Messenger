package st.slex.messenger.di.module

import dagger.Module
import dagger.Provides
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.domain.chats.ChatsDomainMapper
import st.slex.messenger.domain.chats.ChatsDomainResult
import st.slex.messenger.ui.main_screen.ChatsUIResult

@Module
class MapperModule {

    @Provides
    fun bindsChatsDataMapper(): ChatsDataMapper<ChatsDomainResult> =
        ChatsDataMapper.Base()

    @Provides
    fun bindsChatsDomainMapper(): ChatsDomainMapper<ChatsUIResult> =
        ChatsDomainMapper.Base()

}