package st.slex.messenger.main.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.domain.ChatsInteractor

@Module
interface InteractorsModule {

    @Binds
    @ExperimentalCoroutinesApi
    fun bindsChatsInteractorModule(interactor: ChatsInteractor.Base): ChatsInteractor
}