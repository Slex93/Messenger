package st.slex.messenger.main.di.modules

import dagger.Binds
import dagger.Module
import st.slex.messenger.main.domain.ChatsInteractor

@Module
interface InteractorsModule {

    @Binds
    fun bindsChatsInteractorModule(interactor: ChatsInteractor.Base): ChatsInteractor
}