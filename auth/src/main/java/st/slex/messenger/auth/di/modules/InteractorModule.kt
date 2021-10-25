package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import st.slex.messenger.auth.domain.AuthInteractor

@Module
interface InteractorModule {

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractor.Base): AuthInteractor
}