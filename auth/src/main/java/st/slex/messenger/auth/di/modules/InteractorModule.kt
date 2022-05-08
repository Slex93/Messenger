package st.slex.messenger.auth.di.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.domain.interf.AuthInteractor
import st.slex.messenger.auth.domain.real.AuthInteractorImpl

@Module
interface InteractorModule {

    @ExperimentalCoroutinesApi
    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractorImpl): AuthInteractor
}