package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.domain.chats.ChatsInteractor
import st.slex.messenger.domain.contacts.ContactsInteractor
import st.slex.messenger.domain.interactor.impl.AuthInteractorImpl
import st.slex.messenger.domain.interactor.interf.AuthInteractor

@ExperimentalCoroutinesApi
@Module
interface InteractorModule {
    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractorImpl): AuthInteractor

    @Binds
    fun bindsChatsInteractor(interactor: ChatsInteractor.Base): ChatsInteractor

    @Binds
    fun bindsContactsInteractor(interactor: ContactsInteractor.Base): ContactsInteractor
}