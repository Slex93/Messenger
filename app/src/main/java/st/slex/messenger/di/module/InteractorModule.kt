package st.slex.messenger.di.module

import dagger.Binds
import dagger.Module
import st.slex.messenger.domain.interactor.impl.AuthInteractorImpl
import st.slex.messenger.domain.interactor.interf.AuthInteractor

@Module
interface InteractorModule {
    @Binds
    fun bindsLoginInteractor(interactor: AuthInteractorImpl): AuthInteractor
}