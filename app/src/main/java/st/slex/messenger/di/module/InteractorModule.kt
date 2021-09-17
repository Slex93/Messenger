package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.domain.auth.AuthInteractor
import st.slex.messenger.domain.chats.ChatsInteractor
import st.slex.messenger.domain.contacts.ContactsInteractor
import st.slex.messenger.domain.user.UserInteractor

@ExperimentalCoroutinesApi
@Module
interface InteractorModule {
    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractor.Base): AuthInteractor

    @Binds
    fun bindsChatsInteractor(interactor: ChatsInteractor.Base): ChatsInteractor

    @Binds
    fun bindsContactsInteractor(interactor: ContactsInteractor.Base): ContactsInteractor

    @Binds
    fun bindsUserInteractor(interactor: UserInteractor.Base): UserInteractor
}